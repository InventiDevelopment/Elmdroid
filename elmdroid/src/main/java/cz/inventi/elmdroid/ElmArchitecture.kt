package cz.inventi.elmdroid

import android.arch.lifecycle.*
import io.reactivex.Observable
import io.reactivex.Single

/** UI 'loop' implementation that 'runs" given [Component]. */
interface ComponentRuntime<STATE : State, in MSG : Msg> : LifecycleObserver {
    /** Provides always up to date [State]. */
    fun state(): LiveData<STATE>
    /** Dispatch [Msg] to handle. */
    fun dispatch(msg: MSG)
    /** Stops the inner loop, no more state updates. */
    fun clear()
    /** Runtime will be able to call [clear] by itself based on the [lifecycleOwner] */
    fun bindTo(lifecycleOwner: LifecycleOwner) {
        lifecycleOwner.lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy(owner: LifecycleOwner) {
        clear()
    }

    companion object {
        fun <STATE : State, MSG : Msg, CMD : Cmd> create(component: Component<STATE, MSG, CMD>, lifecycleOwner: LifecycleOwner? = null, logLevel: LogLevel = Elmdroid.defaultLogLevel): ComponentRuntime<STATE, MSG> {
            val rxRuntime = RxRuntime(component, logLevel)
            if (lifecycleOwner != null) {
                rxRuntime.bindTo(lifecycleOwner)
            }
            return rxRuntime
        }
    }
}

/** Key class specifying all mayor pars of your elm architecture. */
interface Component<STATE : State, MSG : Msg, CMD : Cmd> {
    /** Initial [State] that will be provided as soon as the [ComponentRuntime] created. */
    fun initState(): STATE
    /** Main transformation method that specifies new immediate [State] and further tasks. */
    fun update(msg: MSG, prevState: STATE): Pair<STATE, CMD?>
    /** Specifies which [Cmd] starts which task. */
    fun call(cmd: CMD): Single<MSG>
    /** List of all [Sub] to be started as soon as the [ComponentRuntime] is created. */
    fun subscriptions(): List<Sub<STATE, MSG>> = listOf()

    // small readability enhancement
    fun STATE.noCmd() = this to null
    infix fun STATE.withCmd(cmd : CMD) = this to cmd
}

/** Simplified component without any asynchronous functionality(task or subscriptions). */
interface SimpleComponent<STATE : State, MSG : Msg>: Component<STATE, MSG, Nothing> {
    override fun update(msg: MSG, prevState: STATE): Pair<STATE, Nothing?> = simpleUpdate(msg, prevState).noCmd()
    override fun call(cmd: Nothing): Single<MSG> = throw IllegalStateException("Call handler not implemented")
    /** Simplified version of update without ability to yield any commands. */
    fun simpleUpdate(msg: MSG, prevState: STATE): STATE
}

sealed class Sub<in STATE : State, MSG : Msg>

/**
 * Subscription that starts with the component creation and ends when runtime is cleared.
 * State changes have no impact on this subscription.
 */
abstract class StatelessSub<in STATE : State, MSG : Msg>: Sub <STATE, MSG>() {
    /** Define subscription [Observable]. */
    abstract operator fun invoke(): Observable<MSG>
}

/** Subscriptions that reacts to [State] changes. */
abstract class StatefulSub<in STATE : State, MSG : Msg> : Sub <STATE, MSG>() {
    /** Define subscription observable for every [State] that is distinct from the previous one, defined by [isDistinct]. */
    abstract operator fun invoke(state: STATE): Observable<MSG>
    /** Defines which [State] changes should trigger [invoke] method. */
    open fun isDistinct(s1: STATE, s2: STATE): Boolean = s1 != s2
}

interface State
interface Msg
interface Cmd




