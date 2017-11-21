package cz.inventi.elmdroid

import android.arch.lifecycle.LiveData

/**
 * Created by tomas.valenta on 11/16/2017.
 */

interface ComponentController<STATE : State, in MSG : Msg> {
    fun state(): LiveData<STATE>
    fun dispatchMsg(msg: MSG)
}

interface Component<STATE : State, in MSG : Msg> {
    fun initState(): STATE
    fun update(msg: MSG, prevState: STATE): STATE
}

interface State
interface Msg