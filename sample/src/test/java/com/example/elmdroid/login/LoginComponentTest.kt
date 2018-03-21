package com.example.elmdroid.login

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import com.example.elmdroid.login.presentation.*
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
import org.mockito.Mockito.`when`
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
    private lateinit var renderer: LoginRenderer
    @Mock lateinit var view : LoginView
    @Mock lateinit var observer: Observer<LoginState>
    @Mock lateinit var emailEditText: EditText
    @Mock lateinit var passwordText: EditText
    @Mock lateinit var loginButton: Button
    @Mock lateinit var progressBar: ProgressBar
    @Mock lateinit var loggedUser: TextView
    @Mock lateinit var timer: TextView
    val baseState = LoginState("", "", false, false, "", "", 0 )

    @Before
    fun setup(){
        MockitoAnnotations.initMocks(this)
        component = LoginComponent()
        runtime = ComponentRuntime.create(component)

        `when`(view.email()).thenReturn(emailEditText)
        `when`(view.password()).thenReturn(passwordText)
        `when`(view.loginButton()).thenReturn(loginButton)
        `when`(view.progressBar()).thenReturn(progressBar)
        `when`(view.loggedUser()).thenReturn(loggedUser)
        `when`(view.timer()).thenReturn(timer)
        renderer = LoginRenderer(view)
    }

    @Test
    fun formChanges() {

        verifyStates(runtime, observer,
                baseState,
                 EmailChanged("a") to baseState.copy(email = "a"),
                EmailChanged("ab") to baseState.copy(email = "ab"),
                PasswordChanged("1") to baseState.copy(email = "ab").copy(password = "1").copy(loginEnabled = true),
                EmailChanged("") to baseState.copy(email = "").copy(password = "1").copy(loginEnabled = false),
                EmailChanged("abc") to baseState.copy(email = "abc").copy(password = "1").copy(loginEnabled = true)
        )
    }
}