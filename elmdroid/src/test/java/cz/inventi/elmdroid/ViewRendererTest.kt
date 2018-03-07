package cz.inventi.elmdroid

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.MutableLiveData
import cz.inventi.elmdroid.utils.RxImmediateSchedulerRule
import cz.inventi.elmdroid.utils.TestState
import cz.inventi.elmdroid.utils.TestView
import cz.inventi.elmdroid.utils.TestViewWithImplLifecycle
import org.junit.Before
import org.junit.ClassRule
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations


class ViewRendererTest {

    @JvmField
    @Rule
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    lateinit var view: TestViewWithImplLifecycle
    private lateinit var stateLiveData: MutableLiveData<TestState>
    private lateinit var renderer: TestRenderer

    companion object {
        @JvmField
        @ClassRule
        val schedulers = RxImmediateSchedulerRule()
    }

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        view = Mockito.spy(TestViewWithImplLifecycle())
        renderer = TestRenderer(view)
        stateLiveData = MutableLiveData()
    }

    @Test
    fun fromIntoBasic() {
        stateLiveData.observeForever(renderer)
        stateLiveData.value = TestState(1, "test1")
        stateLiveData.value = TestState(2, "test2")
        stateLiveData.value = TestState(1, "test1")
        stateLiveData.value = TestState(3, "test1")
        stateLiveData.value = TestState(3, "test3")

        verify(view, times(2)).setText("test1")
        verify(view, times(2)).setNumber(1)
        verify(view, times(1)).setText("test2")
        verify(view, times(1)).setNumber(2)
        verify(view, times(1)).setNumber(3)
        verify(view, times(1)).setText("test3")
    }

    @Test
    fun fromIntoBasicWithNulls() {
        stateLiveData.observeForever(renderer)
        stateLiveData.value = null
        stateLiveData.value = TestState(1, "test1")
        stateLiveData.value = null
        stateLiveData.value = TestState(1, "test1")
        stateLiveData.value = null
        stateLiveData.value = TestState(2, "test2")
        stateLiveData.value = TestState(2, "test3")

        verify(view, times(1)).setText("test1")
        verify(view, times(1)).setNumber(1)
        verify(view, times(1)).setNumber(2)
        verify(view, times(1)).setText("test2")
        verify(view, times(1)).setText("test3")
    }

    @Test
    fun lifecycleUnsubscribeWhenDestroyed() {
        view.markState(Lifecycle.State.INITIALIZED)
        view.markState(Lifecycle.State.CREATED)
        view.markState(Lifecycle.State.STARTED)
        stateLiveData.observe(view, renderer)
        stateLiveData.value = TestState(1, "test1")
        view.markState(Lifecycle.State.DESTROYED)
        stateLiveData.value = TestState(1, "test1")

        verify(view, times(1)).setText("test1")
        verify(view, times(1)).setNumber(1)
    }

    @Test
    fun lifecycleWhenResumed() {
        view.markState(Lifecycle.State.INITIALIZED)
        view.markState(Lifecycle.State.CREATED)
        view.markState(Lifecycle.State.STARTED)
        stateLiveData.observe(view, renderer)
        stateLiveData.value = TestState(1, "test1")
        view.markState(Lifecycle.State.RESUMED)
        stateLiveData.value = TestState(2, "test1")

        verify(view, times(1)).setText("test1")
        verify(view, times(1)).setNumber(1)
        verify(view, times(1)).setNumber(2)
    }
}

class TestRenderer(view: TestView) : BaseViewRenderer<TestView, TestState>(view) {
    override fun TestView.render(state: TestState) {
        state from { it.text } into {
            setText(it)
        } from { it.number } into {
            setNumber(it)
        }
    }
}



