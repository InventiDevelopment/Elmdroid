package com.example.elmdroid.login.presentation

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.elmdroid.R
import com.example.elmdroid.common.setOnTextChangeListener
import cz.inventi.elmdroid.ElmController
import kotlinx.android.synthetic.main.activity_login.*
import android.arch.lifecycle.ViewModelProviders



class LoginVMActivity : BaseLoginActivity() {

    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)

        // observe state
        viewModel.state().observe(this, LoginRenderer(this))

        // setup msg dispatching
        email().setOnTextChangeListener { viewModel.dispatch(EmailChanged(it)) }
        password().setOnTextChangeListener { viewModel.dispatch(PasswordChanged(it)) }
        loginButton().setOnClickListener { viewModel.dispatch(LoginClicked) }
    }

}
