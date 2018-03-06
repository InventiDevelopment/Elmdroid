package cz.inventi.elmdroid.utils

import android.arch.lifecycle.LifecycleOwner
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
}