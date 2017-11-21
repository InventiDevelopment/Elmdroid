package cz.inventi.elmdroid

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.util.logging.Logger

/**
 * Created by tomas.valenta on 11/16/2017.
 */
class ElmController<STATE : State, in MSG : Msg> (component: Component<STATE, MSG>) : ComponentController<STATE, MSG> {

    private val msgRelay: BehaviorRelay<MSG> = BehaviorRelay.create()
    private val stateRelay: BehaviorRelay<STATE> = BehaviorRelay.create()

    private val state: MutableLiveData<STATE> = MutableLiveData()

    init {
        updateStateValue(component.initState())

        Observable.zip(msgRelay, stateRelay, BiFunction<MSG, STATE, STATE> { msg, prevState -> component.update(msg, prevState) })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    updateStateValue(it)
                    Timber.d("State updated to: %s", it)
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
    }
}