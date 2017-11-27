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
    fun clear()
}

interface Component<STATE : State, MSG : Msg, CMD : Cmd> {
    fun initState(): STATE
    fun update(msg: MSG, prevState: STATE): Pair<STATE, CMD?>
    fun call(cmd: CMD): Single<MSG>
    fun subscriptions(): Observable<MSG>


    /**
     * Define how to handle errors emitted by tasks
     */
    fun onError(error: Throwable): MSG

    fun STATE.withoutCmd() = this to null
    infix fun STATE.withCmd(cmd : CMD) = this to cmd

}

interface State
interface Msg
interface Cmd




