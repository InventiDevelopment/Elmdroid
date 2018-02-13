package cz.inventi.elmdroid


object RuntimeFactory {
    fun <STATE : State, MSG : Msg, CMD : Cmd> create(component: Component<STATE, MSG, CMD>, logLevel: LogLevel = LogLevel.NONE): ComponentRuntime<STATE, MSG> {
        return RxRuntime(component, logLevel)
    }
}