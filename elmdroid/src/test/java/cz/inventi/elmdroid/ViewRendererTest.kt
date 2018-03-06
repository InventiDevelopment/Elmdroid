package cz.inventi.elmdroid

import android.arch.core.executor.testing.InstantTaskExecutorRule
import cz.inventi.elmdroid.utils.RxImmediateSchedulerRule
import cz.inventi.elmdroid.utils.TestState
import cz.inventi.elmdroid.utils.TestView
import org.junit.Before
import org.junit.ClassRule
import org.junit.Rule
import org.junit.Test
import org.mockito.MockitoAnnotations


class ViewRendererTest {

    @JvmField
    @Rule
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    lateinit var renderer: BaseViewRenderer<TestView, TestState>

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
        // TODO
    }

}



