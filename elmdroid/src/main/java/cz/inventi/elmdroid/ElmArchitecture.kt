package cz.inventi.elmdroid

import android.arch.lifecycle.LiveData
import io.reactivex.Observable
import io.reactivex.Single

/**
 * Created by tomas.valenta on 11/16/2017.
 */

interface ComponentController<STATE : State, in MSG : Msg> {
    fun state(): LiveData<STATE>
    fun dispatch(msg: MSG)
    fun onCleared()
}

interface Component<STATE : State, MSG : Msg, CMD : Cmd> {
    fun initState(): STATE
    fun update(msg: MSG, prevState: STATE): Pair<STATE, CMD>
    fun call(cmd: CMD): Single<MSG>
    // TODO In the future this should take a state as an parameter to be able to modify subs by changing state
    // TODO but it's hard to implement this behaviour in cases like subs emitting something every second and then change of state
    // TODO fucks everything up
    fun subscriptions(): Observable<MSG>

    /**
     * Defines commands that won's be propagated to call method
     * default implementation doesn't filter any command
     */
    fun filterCmd(cmd: CMD): Boolean = true

    /**
     * Define how to handle errors emitted by tasks
     */
    fun onError(error: Throwable): MSG

}

interface State
interface Msg
interface Cmd


