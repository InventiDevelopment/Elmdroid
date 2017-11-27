package com.example.elmdroid.counter

import cz.inventi.elmdroid.Cmd
import cz.inventi.elmdroid.Component
import cz.inventi.elmdroid.Msg
import cz.inventi.elmdroid.State

class CounterComponent: Component<CounterState, CounterMsg, CounterCmd> {
    override fun initState(): CounterState = CounterState(0)

    override fun update(msg: CounterMsg, prevState: CounterState): Pair<CounterState, CounterCmd?> = when(msg){
        is Increment -> CounterState(prevState.counter + 1).withoutCmd()
        is Decrement -> CounterState(prevState.counter - 1).withoutCmd()
    }
}

data class CounterState(val counter: Int) : State
sealed class CounterMsg : Msg
object Increment : CounterMsg()
object Decrement : CounterMsg()
sealed class CounterCmd: Cmd
