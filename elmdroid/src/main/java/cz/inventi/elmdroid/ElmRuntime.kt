package cz.inventi.elmdroid

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.zipWith
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

/**
 *  Implementation of ComponentRuntime based on RxJava and RxRelay
 */
class ElmRuntime<STATE : State, in MSG : Msg, CMD : Cmd> (component: Component<STATE, MSG, CMD>) : ComponentRuntime<STATE, MSG> {

    private val msgRelay: BehaviorRelay<MSG> = BehaviorRelay.create()
    private val stateRelay: BehaviorRelay<STATE> = BehaviorRelay.create()

    private val state: MutableLiveData<STATE> = MutableLiveData()

    private val compositeDisposable = CompositeDisposable()

    init {
        updateStateValue(component.initState())

        val sharedStateRelay = stateRelay.publish()
                .autoConnect(component.subscriptions().filter { it is StatefulSub<STATE, MSG> }.size + 1)

        compositeDisposable.add(
            msgRelay.zipWith(sharedStateRelay)
                .doOnNext { (msg, prevState) -> Timber.d("now msg: %s and state: %s", msg, prevState) }
                .map { (msg, prevState) -> component.update(msg, prevState) }
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { (newState, _) -> updateStateValue(newState) }
                .filter{ (_, maybeCmd) -> maybeCmd != null }
                .map { (_, cmd) -> cmd }
                .observeOn(Schedulers.newThread())
                .flatMap { cmd -> component.call(cmd).toObservable()}
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe{ msg -> dispatch(msg) }
        )

        component.subscriptions().forEach { sub ->

            val subObs = when (sub) {
                is StatelessSub<STATE, MSG> -> sub()
                is StatefulSub<STATE, MSG> -> {
                    sharedStateRelay
                            .distinctUntilChanged { s1, s2 -> !sub.isDistinct(s1, s2) }
                            .switchMap { state -> sub(state) }
                }
            }

            compositeDisposable.add(
                subObs
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { msg -> dispatch(msg) }
            )
        }
    }

    override fun state(): LiveData<STATE> = state

    override fun clear() = compositeDisposable.clear()

    override fun dispatch(msg: MSG) {
        msgRelay.accept(msg)
        Timber.d("Msg dispatched: %s", msg)
    }

    private fun updateStateValue(stateVal: STATE) {
        state.value = stateVal
        stateRelay.accept(stateVal)
        Timber.d("State updated to: %s", stateVal)
    }
}