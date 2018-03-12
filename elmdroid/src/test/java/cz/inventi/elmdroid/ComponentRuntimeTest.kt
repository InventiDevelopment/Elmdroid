package cz.inventi.elmdroid

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.Observer
import cz.inventi.elmdroid.utils.*
import org.junit.Before
import org.junit.ClassRule
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations


class ComponentRuntimeTest {

    @JvmField
    @Rule
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    lateinit var runtime: ComponentRuntime<TestState, TestMsg>
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
        runtime = Mockito.spy(ComponentRuntime.create(component))
        runtime.bindTo(lifecycleOwner)
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

    @Test
    fun autoClearNotCalled() {
        lifecycleOwner.markState(Lifecycle.State.INITIALIZED)
        lifecycleOwner.markState(Lifecycle.State.CREATED)
        lifecycleOwner.markState(Lifecycle.State.RESUMED)
        verify(runtime, never()).clear()
    }
}

