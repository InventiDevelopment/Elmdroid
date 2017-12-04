package com.example.elmdroid.login.presentation

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.elmdroid.R
import com.example.elmdroid.common.setOnTextChangeListener
import kotlinx.android.synthetic.main.activity_login.*
import android.arch.lifecycle.ViewModelProviders



class LoginVMActivity : AppCompatActivity(), LoginView {

    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportActionBar?.title = getString(R.string.complex_login_with_viewmodel)

        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)

        // observe state
        viewModel.state().observe(this, LoginRenderer(this))

        // setup msg dispatching
        email().setOnTextChangeListener { viewModel.dispatch(EmailChanged(it)) }
        password().setOnTextChangeListener { viewModel.dispatch(PasswordChanged(it)) }
        loginButton().setOnClickListener { viewModel.dispatch(LoginClicked) }
    }

    override fun email() = email
    override fun password() = password
    override fun loginButton() = loginButton
    override fun progressBar() = progressBar
    override fun loggedUser() = loggedUser
    override fun timer() = timer
    override fun showUserMsg(userMsg: String) = Toast.makeText(this, userMsg, Toast.LENGTH_LONG).show()

}
