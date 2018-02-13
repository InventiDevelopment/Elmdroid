package com.example.elmdroid.login.presentation

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.example.elmdroid.R
import com.example.elmdroid.common.setOnTextChangeListener
import cz.inventi.elmdroid.ElmRuntime
import cz.inventi.elmdroid.LogLevel
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), LoginView  {

    private lateinit var runtime: ElmRuntime<LoginState, LoginMsg, LoginCmd>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportActionBar?.title = getString(R.string.complex_login)

        runtime = ElmRuntime(LoginComponent(), LogLevel.FULL)

        // observe state
        runtime.state().observe(this, LoginRenderer(this))

        // setup msg dispatching
        email().setOnTextChangeListener { runtime.dispatch(EmailChanged(it)) }
        password().setOnTextChangeListener { runtime.dispatch(PasswordChanged(it)) }
        loginButton().setOnClickListener { runtime.dispatch(LoginClicked) }
    }

    override fun onDestroy() {
        super.onDestroy()
        runtime.clear()
    }

    override fun email() = email
    override fun password() = password
    override fun loginButton() = loginButton
    override fun progressBar() = progressBar
    override fun loggedUser() = loggedUser
    override fun timer() = timer
    override fun showUserMsg(userMsg: String) = Toast.makeText(this, userMsg, Toast.LENGTH_LONG).show()
}
