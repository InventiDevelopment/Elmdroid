package cz.inventi.elmdroid

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel

/**
 * Created by Tomas Valenta on 24.11.2017.
 */
open class ElmViewModel<STATE : State, in MSG : Msg, CMD : Cmd> (component: Component<STATE, MSG, CMD>) :
        ViewModel(),
        ComponentRuntime<STATE, MSG> by ElmRuntime<STATE, MSG, CMD>(component) {

    override fun onCleared() {
        super.onCleared()
        clear()
    }
}

abstract class ElmBaseViewModel<STATE : State, MSG : Msg, CMD : Cmd> : ViewModel(),
        Component<STATE, MSG, CMD>,
        ComponentRuntime<STATE, MSG> {

    private val runtime = ElmRuntime(this)

    override fun state(): LiveData<STATE> = runtime.state()

    override fun dispatch(msg: MSG) = runtime.dispatch(msg)

    override fun clear() = runtime.clear()
}

abstract class ElmSimpleBaseViewModel<STATE : State, MSG : Msg> : ViewModel(),
        SimpleComponent<STATE, MSG>,
        ComponentRuntime<STATE, MSG> {

    private val runtime = ElmRuntime(this)

    override fun state(): LiveData<STATE> = runtime.state()

    override fun dispatch(msg: MSG) = runtime.dispatch(msg)

    override fun clear() = runtime.clear()
}