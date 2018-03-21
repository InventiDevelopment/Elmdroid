package com.example.elmdroid.login.presentation

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.*
import com.example.elmdroid.R
import com.example.elmdroid.common.setOnTextChangeListener
import cz.inventi.elmdroid.getViewModelFor
import kotlinx.android.synthetic.main.activity_login.*
import net.semanticer.renderit.renderit


class LoginVMActivity : AppCompatActivity(), LoginView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportActionBar?.title = getString(R.string.complex_login_with_viewmodel)

        val viewModel = getViewModelFor(LoginComponent())

        // observe state
        renderit(viewModel.state(), LoginRenderer(this))

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
