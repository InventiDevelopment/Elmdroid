package com.example.elmdroid.utils

import android.arch.lifecycle.Observer
import cz.inventi.elmdroid.ComponentRuntime
import cz.inventi.elmdroid.Msg
import cz.inventi.elmdroid.State
import org.mockito.Mockito


fun <M: Msg> ComponentRuntime<*, M>.dispatchMultiple(msgList: List<M>) {
    for (msg in msgList) {
        dispatch(msg)
    }
}

fun <S> Observer<S>.verifyStateChanges(stateList: List<S>) {
    val inOrder = Mockito.inOrder(this)
    for (state in stateList) {
        inOrder.verify(this).onChanged(state)
    }
}

fun <M: Msg, S: State> verifyStates(componentRuntime: ComponentRuntime<S, M>, observer: Observer<S>, initState: S, vararg msgToState: Pair<M, S>) {
    componentRuntime.state().observeForever(observer)
    componentRuntime.dispatchMultiple(msgToState.map { it.first })
    observer.verifyStateChanges(listOf(initState) + msgToState.map { it.second })
}