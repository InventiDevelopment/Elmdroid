package cz.inventi.elmdroid

import android.arch.lifecycle.LifecycleOwner


object RuntimeFactory {
    var defaultLogLevel = LogLevel.NONE
    fun <STATE : State, MSG : Msg, CMD : Cmd> create(component: Component<STATE, MSG, CMD>, lifecycleOwner: LifecycleOwner? = null, logLevel: LogLevel = defaultLogLevel): ComponentRuntime<STATE, MSG> {
        val rxRuntime = RxRuntime(component, logLevel)
        if (lifecycleOwner != null) {
            rxRuntime.bindTo(lifecycleOwner)
        }
        return rxRuntime
    }
}