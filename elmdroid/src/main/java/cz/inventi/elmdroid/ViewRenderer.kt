package cz.inventi.elmdroid

import android.arch.lifecycle.DefaultLifecycleObserver
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer

interface ViewRenderer<in V : LifecycleOwner, S: State> : Observer<S>, DefaultLifecycleObserver {
    fun V.render(state: S)
    infix fun <T> S.from(getter: (S) -> T): Triple<S, S?, (S) -> T>
    infix fun <S, T> Triple<S, S?, (S) -> T>.into(apply: (T) -> Unit): S
}

abstract class BaseViewRenderer<in V : LifecycleOwner, S: State> (private var view: V?) : ViewRenderer<V, S> {

    private var previouslyRenderedState: S? = null

    init {
        view?.lifecycle?.addObserver(this)
    }

    override fun onChanged(state: S?) {
        view?.apply {
            if (state != null) {
                render(state)
                previouslyRenderedState = state
            }
        }
    }

    override fun onDestroy(owner: LifecycleOwner) {
        view = null
    }

    override fun onStop(owner: LifecycleOwner) {
        previouslyRenderedState = null
    }

    override infix fun <T> S.from(getter: (S) -> T): Triple<S, S?, (S) -> T> = Triple(this, previouslyRenderedState, getter)
    override infix fun <S, T> Triple<S, S?, (S) -> T>.into(apply: (T) -> Unit): S {
        val (state, prevState, getter) = this
        val newValue = getter(state)
        if (prevState == null || getter(prevState) != newValue) {
            apply(newValue)
        }
        return state
    }
}