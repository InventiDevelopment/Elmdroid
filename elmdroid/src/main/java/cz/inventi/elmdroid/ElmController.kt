package cz.inventi.elmdroid

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
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

    init {
        updateStateValue(component.initState())

        msgRelay.zipWith(stateRelay)
                .map { (msg, prevState) -> component.update(msg, prevState) }
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { (newState, _) -> updateStateValue(newState) }
                .map { (_, cmd) -> cmd }
                .filter { component.filterCmd(it) }
                .observeOn(Schedulers.newThread())
                .flatMap { cmd -> processCmd(cmd, component)}
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { resultMsg ->
//                    updateStateValue(stateRelay.value) // to create pair for zip function for dispatch processing
                    dispatch(resultMsg)
                }

    }

    override fun state(): LiveData<STATE> = state

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
                    Timber.d("Error %s after cmd ", error, cmd)
                    Single.just(component.handleCmdError(error, cmd))
                }
                .toObservable()
    }
}