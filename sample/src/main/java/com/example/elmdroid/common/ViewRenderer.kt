package com.example.elmdroid.common

import android.arch.lifecycle.DefaultLifecycleObserver
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer

/**
 * Created by tomas.valenta on 11/27/2017.
 */
abstract class ViewRenderer<in V : LifecycleOwner, S> (private var view: V?) : Observer<S>, DefaultLifecycleObserver {
    init {
        view?.lifecycle?.addObserver(this)
    }

    override fun onChanged(state: S?) {
        view?.apply {
            state?.apply {
                render(state)
            }
        }
    }

    override fun onDestroy(owner: LifecycleOwner) {
        view = null
    }

    abstract fun V.render(state: S)
}