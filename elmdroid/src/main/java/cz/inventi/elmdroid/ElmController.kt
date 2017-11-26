package cz.inventi.elmdroid

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
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
                .map { (msg, prevState) -> component.update(msg, prevState) }
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { (newState, _) -> updateStateValue(newState) }
                .filter{ (_, maybeCmd) -> maybeCmd != null }
                .map { (_, cmd) -> cmd }
                .observeOn(Schedulers.newThread())
                .flatMap { cmd -> processCmd(cmd, component)}
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { dispatch(it) }
        )

        compositeDisposable.add(
            component.subscriptions()
                .onErrorResumeNext { error: Throwable -> Observable.just(component.onError(error)) }
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { dispatch(it) }
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

    private fun processCmd(cmd: CMD, component: Component<STATE, MSG, CMD>): Observable<MSG>? {
        Timber.d("Call cmd: %s", cmd)
        return component.call(cmd)
                .onErrorResumeNext { error ->
                    Timber.d("Error %s after cmd %s", error, cmd)
                    Single.just(component.onError(error))
                }
                .toObservable()
    }
}