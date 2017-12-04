package cz.inventi.elmdroid

import android.arch.lifecycle.LiveData
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import io.reactivex.Single
import io.reactivex.functions.Predicate

interface ComponentRuntime<STATE : State, in MSG : Msg> {
    fun state(): LiveData<STATE>
    fun dispatch(msg: MSG)
    fun clear()
}

interface Component<STATE : State, MSG : Msg, CMD : Cmd> {
    fun initState(): STATE
    fun update(msg: MSG, prevState: STATE): Pair<STATE, CMD?>
    fun call(cmd: CMD): Single<MSG> = throw IllegalStateException("Call handler not implemented")
    fun subscriptions(): List<Sub<STATE, MSG, *>> = listOf()

    // small readability enhancement
    fun STATE.noCmd() = this to null
    infix fun STATE.withCmd(cmd : CMD) = this to cmd
}

interface Sub <STATE : State, MSG : Msg, T> {
    fun createObservable(): Observable<T>
    fun changeWithState(): ObservableTransformer<Pair<STATE, T>, MSG>
    fun filterState(newState: STATE, oldState: STATE): Boolean
}

abstract class StatelessSub<STATE : State, MSG : Msg> : Sub <STATE, MSG, MSG> {
    override fun changeWithState(): ObservableTransformer<Pair<STATE, MSG>, MSG> = ObservableTransformer<Pair<STATE, MSG>, MSG> { upstream: Observable<Pair<STATE, MSG>> ->
        return@ObservableTransformer upstream.map { (_, msg) -> msg }
    }
    override fun filterState(newState: STATE, oldState: STATE) = false
}

interface State
interface Msg
interface Cmd




