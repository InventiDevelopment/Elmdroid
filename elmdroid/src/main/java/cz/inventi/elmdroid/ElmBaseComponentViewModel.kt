package cz.inventi.elmdroid

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel

/**
 * Created by Tomas Valenta on 26.11.2017.
 */
abstract class ElmBaseComponentViewModel <STATE : State, MSG : Msg, CMD : Cmd> : ViewModel(),
        Component<STATE, MSG, CMD>,
        ComponentRuntime<STATE, MSG> {

    private val controller = ElmRuntime(this)

    override fun state(): LiveData<STATE> = controller.state()

    override fun dispatch(msg: MSG) = controller.dispatch(msg)

    override fun clear() = controller.clear()
}