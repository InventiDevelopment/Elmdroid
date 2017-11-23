package com.example.elmdroid.login.presentation

import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView

/**
 * Created by tomas.valenta on 11/21/2017.
 */
interface LoginView {
    fun email(): EditText
    fun password(): EditText
    fun loginButton(): Button
    fun progressBar(): ProgressBar
    fun loggedUser(): TextView
    fun showUserMsg(userMsg: String)
}