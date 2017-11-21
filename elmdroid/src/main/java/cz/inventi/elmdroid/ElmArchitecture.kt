package cz.inventi.elmdroid

import android.arch.lifecycle.LiveData
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
}

interface State
interface Msg
interface Cmd
