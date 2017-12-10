package cz.inventi.elmdroid

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel

/**
 * Created by Tomas Valenta on 26.11.2017.
 */
abstract class ElmBaseViewModel<STATE : State, MSG : Msg, CMD : Cmd> : ViewModel(),
        Component<STATE, MSG, CMD>,
        ComponentRuntime<STATE, MSG> {

    private val runtime = ElmRuntime(this)

    override fun state(): LiveData<STATE> = runtime.state()

    override fun dispatch(msg: MSG) = runtime.dispatch(msg)

    override fun clear() = runtime.clear()
}