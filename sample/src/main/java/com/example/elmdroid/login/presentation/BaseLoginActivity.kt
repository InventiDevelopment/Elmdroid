package com.example.elmdroid.login.presentation

import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.example.elmdroid.R
import kotlinx.android.synthetic.main.activity_login.*

abstract class BaseLoginActivity : AppCompatActivity(), LoginView {

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.activity_login)
    }

    override fun email() = email
    override fun password() = password
    override fun loginButton() = loginButton
    override fun progressBar() = progressBar
    override fun loggedUser() = loggedUser
    override fun timer() = timer
    override fun showUserMsg(userMsg: String) = Toast.makeText(this, userMsg, Toast.LENGTH_LONG).show()
}