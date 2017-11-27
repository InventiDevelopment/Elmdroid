package com.example.elmdroid.login.presentation

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.elmdroid.R
import com.example.elmdroid.common.setOnTextChangeListener
import cz.inventi.elmdroid.ElmController
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseLoginActivity() {

    private lateinit var controller: ElmController<LoginState, LoginMsg, LoginCmd>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        controller = ElmController(LoginComponent())

        // observe state
        controller.state().observe(this, LoginRenderer(this))

        // setup msg dispatching
        email().setOnTextChangeListener { controller.dispatch(EmailChanged(it)) }
        password().setOnTextChangeListener { controller.dispatch(PasswordChanged(it)) }
        loginButton().setOnClickListener { controller.dispatch(LoginClicked) }
    }

    override fun onDestroy() {
        super.onDestroy()
        controller.clear()
    }
}
