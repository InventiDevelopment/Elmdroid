package com.example.elmdroid.login.presentation

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.*
import com.example.elmdroid.R
import com.example.elmdroid.common.setOnTextChangeListener
import kotlinx.android.synthetic.main.activity_login.*


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

    override fun email(): EditText = email
    override fun password(): EditText = password
    override fun loginButton(): Button = loginButton
    override fun progressBar(): ProgressBar = progressBar
    override fun loggedUser(): TextView = loggedUser
    override fun timer(): TextView = timer
    override fun showUserMsg(userMsg: String) = Toast.makeText(this, userMsg, Toast.LENGTH_LONG).show()

}
