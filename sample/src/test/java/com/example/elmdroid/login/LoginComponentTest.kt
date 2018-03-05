package com.example.elmdroid.login

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import com.example.elmdroid.login.presentation.*
import com.example.elmdroid.utils.RxImmediateSchedulerRule
import com.example.elmdroid.utils.verifyStates
import cz.inventi.elmdroid.ComponentRuntime
import cz.inventi.elmdroid.RuntimeFactory
import org.junit.Before
import org.junit.ClassRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.MockitoAnnotations


@RunWith(JUnit4::class)
class LoginComponentTest {

    @JvmField
    @Rule
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    companion object {
        @JvmField
        @ClassRule
        val schedulers = RxImmediateSchedulerRule()
    }

    private lateinit var component: LoginComponent
    private lateinit var runtime: ComponentRuntime<LoginState, LoginMsg>
    @Mock lateinit var observer: Observer<LoginState>

    @Before
    fun setup(){
        MockitoAnnotations.initMocks(this)
        component = LoginComponent()
        runtime = RuntimeFactory.create(component)
    }

    @Test
    fun formChanges() {
        val startState = LoginState("", "", false, false, "", "", 0 )
        verifyStates(runtime, observer,
                startState,
                 EmailChanged("a") to startState.copy(email = "a"),
                EmailChanged("ab") to startState.copy(email = "ab"),
                PasswordChanged("1") to startState.copy(email = "ab").copy(password = "1").copy(loginEnabled = true),
                EmailChanged("") to startState.copy(email = "").copy(password = "1").copy(loginEnabled = false),
                EmailChanged("abc") to startState.copy(email = "abc").copy(password = "1").copy(loginEnabled = true)
        )
    }
}