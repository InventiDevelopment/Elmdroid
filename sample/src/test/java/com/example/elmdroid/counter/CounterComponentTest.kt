package com.example.elmdroid.counter

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import com.example.elmdroid.utils.RxImmediateSchedulerRule
import com.example.elmdroid.utils.verifyStates
import cz.inventi.elmdroid.ComponentRuntime
import org.junit.Before
import org.junit.ClassRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.MockitoAnnotations


@RunWith(JUnit4::class)
class CounterComponentTest {

    @JvmField
    @Rule
    @get:Rule val instantExecutorRule = InstantTaskExecutorRule()

    companion object {
        @JvmField
        @ClassRule
        val schedulers = RxImmediateSchedulerRule()
    }

    private lateinit var component: CounterComponent
    private lateinit var runtime: ComponentRuntime<CounterState, CounterMsg>
    @Mock lateinit var observer: Observer<CounterState>

    @Before
    fun setup(){
        MockitoAnnotations.initMocks(this)
        component = CounterComponent()
        runtime = ComponentRuntime.create(component)
    }

    @Test
    fun simpleUpdate() {
        verifyStates(runtime, observer,
                CounterState(0),
                Increment to CounterState(1),
                Decrement to CounterState(0),
                Decrement to CounterState(-1),
                Decrement to CounterState(-2),
                Increment to CounterState(-1),
                Increment to CounterState(0)
        )
    }
}