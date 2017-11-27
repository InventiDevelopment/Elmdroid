package cz.inventi.elmdroid

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.rxkotlin.zipWith
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

/**
 *  Implementation of ComponentController based on RxJava and RxRelay
 */
class ElmController<STATE : State, in MSG : Msg, CMD : Cmd> (component: Component<STATE, MSG, CMD>) : ComponentController<STATE, MSG> {

    private val msgRelay: BehaviorRelay<MSG> = BehaviorRelay.create()
    private val stateRelay: BehaviorRelay<STATE> = BehaviorRelay.create()

    private val state: MutableLiveData<STATE> = MutableLiveData()

    private val compositeDisposable = CompositeDisposable()

    init {
        updateStateValue(component.initState())

        compositeDisposable.add(
            msgRelay.zipWith(stateRelay)
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

        compositeDisposable.add(
            component.subscriptions()
//                .onErrorResumeNext { error: Throwable -> Observable.just(component.onError(error)) } // this stops subscriptions
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { msg -> dispatch(msg) }
        )
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

    // TODO think again about error handling and if it make's sense to use on error functionality this way, maybe those errors should be fatal
//    private fun processCmd(cmd: CMD, component: Component<STATE, MSG, CMD>): Observable<MSG>? {
//        Timber.d("Call cmd: %s", cmd)
//        return component.call(cmd)
//                // TODO related discussion https://github.com/ReactiveX/RxJava/issues/3870
//                .onErrorReturn { error ->
//                    component.onError(error)
//                }
//                .toObservable()
//    }
}