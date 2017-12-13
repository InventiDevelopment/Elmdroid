package cz.inventi.elmdroid

import android.arch.lifecycle.LiveData
import io.reactivex.Observable
import io.reactivex.Single

interface ComponentRuntime<STATE : State, in MSG : Msg> {
    fun state(): LiveData<STATE>
    fun dispatch(msg: MSG)
    fun clear()
}

interface Component<STATE : State, MSG : Msg, CMD : Cmd> {
    fun initState(): STATE
    fun update(msg: MSG, prevState: STATE): Pair<STATE, CMD?>
    fun call(cmd: CMD): Single<MSG> = throw IllegalStateException("Call handler not implemented")
    fun subscriptions(): List<Sub<STATE, MSG>> = listOf()

    // small readability enhancement
    fun STATE.noCmd() = this to null
    infix fun STATE.withCmd(cmd : CMD) = this to cmd
}


interface SimpleComponent<STATE : State, MSG : Msg>: Component<STATE, MSG, Nothing> {
    override fun update(msg: MSG, prevState: STATE): Pair<STATE, Nothing?> = simpleUpdate(msg, prevState).noCmd()
    fun simpleUpdate(msg: MSG, prevState: STATE): STATE
}

sealed class Sub<in STATE : State, MSG : Msg>

abstract class StatelessSub<in STATE : State, MSG : Msg>: Sub <STATE, MSG>() {
    abstract operator fun invoke(): Observable<MSG>
}

abstract class StatefulSub<in STATE : State, MSG : Msg> : Sub <STATE, MSG>() {
    abstract operator fun invoke(state: STATE): Observable<MSG>
    open fun isDistinct(s1: STATE, s2: STATE): Boolean = s1 != s2
}

interface State
interface Msg
interface Cmd




