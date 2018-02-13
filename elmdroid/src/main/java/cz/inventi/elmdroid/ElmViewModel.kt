package cz.inventi.elmdroid

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel


open class ElmViewModel<STATE : State, in MSG : Msg, CMD : Cmd> (component: Component<STATE, MSG, CMD>, logLevel: LogLevel = LogLevel.NONE) :
        ViewModel(),
        ComponentRuntime<STATE, MSG> by RxRuntime<STATE, MSG, CMD>(component, logLevel) {

    override fun onCleared() {
        super.onCleared()
        clear()
    }
}

abstract class ElmBaseViewModel<STATE : State, MSG : Msg, CMD : Cmd>(logLevel: LogLevel = LogLevel.NONE) : ViewModel(),
        Component<STATE, MSG, CMD>,
        ComponentRuntime<STATE, MSG> {

    private val runtime = RxRuntime(this, logLevel)

    override fun state(): LiveData<STATE> = runtime.state()

    override fun dispatch(msg: MSG) = runtime.dispatch(msg)

    override fun clear() = runtime.clear()
}

abstract class ElmSimpleBaseViewModel<STATE : State, MSG : Msg>(logLevel: LogLevel = LogLevel.NONE): ViewModel(),
        SimpleComponent<STATE, MSG>,
        ComponentRuntime<STATE, MSG> {

    private val runtime = RxRuntime(this, logLevel)

    override fun state(): LiveData<STATE> = runtime.state()

    override fun dispatch(msg: MSG) = runtime.dispatch(msg)

    override fun clear() = runtime.clear()
}