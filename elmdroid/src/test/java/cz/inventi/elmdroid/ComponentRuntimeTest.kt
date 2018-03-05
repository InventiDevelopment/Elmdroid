package cz.inventi.elmdroid

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LifecycleRegistry
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
import org.mockito.Spy


class ComponentRuntimeTest {

    @JvmField
    @Rule
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Spy lateinit var runtime: ComponentRuntime<TestState, TestMsg>
    @Mock lateinit var observer: Observer<TestState>
    @Mock lateinit var component: Component<TestState, TestMsg, TestCmd>

    private lateinit var lifecycleOwner: TestLifecycleOwner

    companion object {
        @JvmField
        @ClassRule
        val schedulers = RxImmediateSchedulerRule()
    }

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        `when`(component.initState()).thenReturn(TestState(3, "init"))
        lifecycleOwner = TestLifecycleOwner()
        runtime = RuntimeFactory.create(component)
    }

    @Test
    fun initState() {
        runtime.state().observeForever(observer)
        verify(observer).onChanged(TestState(3, "init"))
    }

    @Test
    fun autoClear() {
        lifecycleOwner.markState(Lifecycle.State.CREATED)
        lifecycleOwner.markState(Lifecycle.State.DESTROYED)
        verify(runtime).clear()
    }
}

data class TestState(val number: Int, val text: String): State

sealed class TestMsg : Msg
object ObjMsg : TestMsg()
data class NumberMsg(val number: Int) : TestMsg()

sealed class TestCmd: Cmd
data class NumberCmd(val num: Int): TestCmd()

class TestLifecycleOwner : LifecycleOwner {
    private val lifecycleRegistry: LifecycleRegistry = LifecycleRegistry(this)
    override fun getLifecycle(): Lifecycle = lifecycleRegistry
    fun markState(state: Lifecycle.State) {
        lifecycleRegistry.markState(state)
    }
}

