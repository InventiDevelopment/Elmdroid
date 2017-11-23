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
}

interface Component<STATE : State, MSG : Msg, CMD : Cmd> {
    fun initState(): STATE
    fun update(msg: MSG, prevState: STATE): Pair<STATE, CMD>
    fun call(cmd: CMD): Single<MSG>
    fun subscriptions(state: STATE): Observable<MSG>

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


