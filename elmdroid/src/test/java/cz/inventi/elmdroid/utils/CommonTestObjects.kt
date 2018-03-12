package cz.inventi.elmdroid.utils

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LifecycleRegistry
import cz.inventi.elmdroid.Cmd
import cz.inventi.elmdroid.Msg
import cz.inventi.elmdroid.State

data class TestState(val number: Int, val text: String): State

sealed class TestMsg : Msg
object ObjMsg : TestMsg()
data class NumberMsg(val number: Int) : TestMsg()

sealed class TestCmd: Cmd
data class NumberCmd(val num: Int): TestCmd()

interface TestView: LifecycleOwner {
    fun setText(text: String)
    fun setNumber(number: Int)
}

class TestViewWithImplLifecycle : TestLifecycleOwner(), TestView {
    override fun setText(text: String) {}
    override fun setNumber(number: Int) {}
}

open class TestLifecycleOwner : LifecycleOwner, Lifecycle() {
    private val lifecycleRegistry: LifecycleRegistry = LifecycleRegistry(this)
    override fun getLifecycle(): Lifecycle = lifecycleRegistry
    fun markState(state: Lifecycle.State) {
        lifecycleRegistry.markState(state)
    }
    override fun addObserver(observer: LifecycleObserver) {
        lifecycleRegistry.addObserver(observer)
    }

    override fun removeObserver(observer: LifecycleObserver) {
        lifecycleRegistry.removeObserver(observer)
    }

    override fun getCurrentState(): State {
        return lifecycleRegistry.currentState
    }
}