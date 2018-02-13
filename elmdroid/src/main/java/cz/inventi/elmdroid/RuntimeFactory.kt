package cz.inventi.elmdroid


object RuntimeFactory {
    var defaultLogLevel = LogLevel.NONE
    fun <STATE : State, MSG : Msg, CMD : Cmd> create(component: Component<STATE, MSG, CMD>, logLevel: LogLevel = defaultLogLevel): ComponentRuntime<STATE, MSG> {
        return RxRuntime(component, logLevel)
    }
}