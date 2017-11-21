package com.example.elmdroid.login

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.elmdroid.R
import com.example.elmdroid.common.setOnTextChangeListener
import cz.inventi.elmdroid.ElmController
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), LoginView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val controller = ElmController(LoginComponent())

        // observe state
        controller.state().observe(this, LoginRenderer(this))

        // setup msg dispatching
        email().setOnTextChangeListener { controller.dispatch(EmailChanged(it)) }
        password().setOnTextChangeListener { controller.dispatch(PasswordChanged(it)) }
        loginButton().setOnClickListener { controller.dispatch(LoginClicked) }
    }

    override fun email() = email
    override fun password() = password
    override fun loginButton() = loginButton
    override fun progressBar() = progressBar
    override fun loggedUser() = loggedUser
}
