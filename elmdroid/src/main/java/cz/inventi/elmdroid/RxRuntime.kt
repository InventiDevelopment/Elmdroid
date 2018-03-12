package cz.inventi.elmdroid

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.zipWith
import io.reactivex.schedulers.Schedulers

/**
 *  Implementation of ComponentRuntime based on RxJava and RxRelay
 */
internal class RxRuntime<STATE : State, in MSG : Msg, CMD : Cmd> (component: Component<STATE, MSG, CMD>, private val logLevel: LogLevel = LogLevel.NONE) : ComponentRuntime<STATE, MSG> {

    private val msgRelay: BehaviorRelay<MSG> = BehaviorRelay.create()
    private val stateRelay: BehaviorRelay<STATE> = BehaviorRelay.create()

    private val state: MutableLiveData<STATE> = MutableLiveData()

    private val compositeDisposable = CompositeDisposable()

    init {
        updateStateValue(component.initState())

        val sharedStateRelay = stateRelay.publish()
                .autoConnect(component.subscriptions().filter { it is StatefulSub<STATE, MSG> }.size + 1)

        // setup main app "loop" without subscriptions
        compositeDisposable.add(
            msgRelay.zipWith(sharedStateRelay)
                .map { (msg, prevState) -> component.update(msg, prevState) }
                .subscribeNewObserveMain()
                .doOnNext { (newState, _) -> updateStateValue(newState) }
                .compose(cmdToMsg(component))
                .subscribe{ msg -> dispatch(msg) }
        )

        // handle subscriptions
        component.subscriptions().forEach { sub ->
            val subObs: Observable<MSG> = createSubObs(sub, sharedStateRelay)
            val subDisposable = subObs
                .subscribeNewObserveMain()
                .doOnSubscribe { log(logLevel, LogLevel.FULL, TAG,"Subscribed sub: $sub") }
                .subscribe { msg -> dispatch(msg) }
            compositeDisposable.add(subDisposable)
        }
    }

    override fun state(): LiveData<STATE> = state

    override fun clear() {
        compositeDisposable.clear()
        log(logLevel, LogLevel.BASIC, TAG, "Runtime cleared")
    }

    override fun dispatch(msg: MSG) {
        msgRelay.accept(msg)
        log(logLevel, LogLevel.BASIC, TAG,"Msg dispatched: $msg")
    }

    private fun updateStateValue(stateVal: STATE) {
        state.value = stateVal
        stateRelay.accept(stateVal)
        log(logLevel, LogLevel.BASIC, TAG, "State updated to: $stateVal")
    }

    private fun cmdToMsg(component: Component<STATE, MSG, CMD>): ObservableTransformer<Pair<STATE, CMD?>, MSG> {
        return ObservableTransformer { obs ->
            obs.filter{ (_, maybeCmd) -> maybeCmd != null }
                .map { (_, cmd) -> cmd }
                .observeOn(Schedulers.newThread())
                .doOnNext { cmd -> log(logLevel, LogLevel.BASIC, TAG, "Calling cmd: $cmd") }
                .flatMap { cmd -> component.call(cmd).toObservable()}
                .observeOn(AndroidSchedulers.mainThread())
        }
    }

    private fun createSubObs(
        sub: Sub<STATE, MSG>,
        sharedStateRelay: Observable<STATE>
    ): Observable<MSG> {
        return when (sub) {
            is StatelessSub<STATE, MSG> -> sub()
            is StatefulSub<STATE, MSG> -> {
                sharedStateRelay
                    .distinctUntilChanged { s1, s2 -> !sub.isDistinct(s1, s2) }
                    .doOnNext{ state -> log(logLevel, LogLevel.FULL, TAG,  "New state $state for StatefulSub $sub to handle")}
                    .switchMap { state -> sub(state) }
            }
        }
    }

    companion object {
        const val TAG = "ComponentRuntime"
    }


}