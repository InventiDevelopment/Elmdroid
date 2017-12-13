package com.example.elmdroid.counter

import cz.inventi.elmdroid.*

class CounterComponent: SimpleComponent<CounterState, CounterMsg> {
    override fun initState(): CounterState = CounterState(0)

    override fun simpleUpdate(msg: CounterMsg, prevState: CounterState): CounterState = when(msg){
        is Increment -> CounterState(prevState.counter + 1)
        is Decrement -> CounterState(prevState.counter - 1)
    }
}

data class CounterState(val counter: Int) : State
sealed class CounterMsg : Msg
object Increment : CounterMsg()
object Decrement : CounterMsg()
