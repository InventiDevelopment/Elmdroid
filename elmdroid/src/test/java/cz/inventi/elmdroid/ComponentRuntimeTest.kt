package cz.inventi.elmdroid

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import cz.inventi.elmdroid.utils.RxImmediateSchedulerRule
import org.junit.Before
import org.junit.ClassRule
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations


class ComponentRuntimeTest {

    @JvmField
    @Rule
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var runtime: ComponentRuntime<TestState, TestMsg>
    @Mock lateinit var observer: Observer<TestState>
    @Mock lateinit var component: Component<TestState, TestMsg, TestCmd>



    companion object {
        @JvmField
        @ClassRule
        val schedulers = RxImmediateSchedulerRule()
    }

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

    }

    @Test
    fun initState() {
        `when`(component.initState()).thenReturn(TestState(3, "init"))
        runtime = RuntimeFactory.create(component)
        runtime.state().observeForever(observer)
        verify(observer).onChanged(TestState(3, "init"))
    }

}

data class TestState(val number: Int, val text: String): State

sealed class TestMsg : Msg
object ObjMsg : TestMsg()
data class NumberMsg(val number: Int) : TestMsg()

sealed class TestCmd: Cmd
data class NumberCmd(val num: Int): TestCmd()

