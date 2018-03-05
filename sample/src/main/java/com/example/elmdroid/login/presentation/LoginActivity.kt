package com.example.elmdroid.login.presentation

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.*
import com.example.elmdroid.R
import com.example.elmdroid.common.setOnTextChangeListener
import cz.inventi.elmdroid.ComponentRuntime
import cz.inventi.elmdroid.RuntimeFactory
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), LoginView  {

    private lateinit var runtime: ComponentRuntime<LoginState, LoginMsg>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportActionBar?.title = getString(R.string.complex_login)

        runtime = RuntimeFactory.create(LoginComponent(), this)

        // observe state
        runtime.state().observe(this, LoginRenderer(this))

        // setup msg dispatching
        email().setOnTextChangeListener { runtime.dispatch(EmailChanged(it)) }
        password().setOnTextChangeListener { runtime.dispatch(PasswordChanged(it)) }
        loginButton().setOnClickListener { runtime.dispatch(LoginClicked) }
    }

    override fun email(): EditText = email
    override fun password(): EditText = password
    override fun loginButton(): Button = loginButton
    override fun progressBar(): ProgressBar = progressBar
    override fun loggedUser(): TextView = loggedUser
    override fun timer(): TextView = timer
    override fun showUserMsg(userMsg: String) = Toast.makeText(this, userMsg, Toast.LENGTH_LONG).show()
}
