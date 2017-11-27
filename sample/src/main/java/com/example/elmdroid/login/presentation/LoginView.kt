package com.example.elmdroid.login.presentation

import android.arch.lifecycle.LifecycleOwner
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView

/**
 * Created by tomas.valenta on 11/21/2017.
 */
interface LoginView : LifecycleOwner {
    fun email(): EditText
    fun password(): EditText
    fun loginButton(): Button
    fun progressBar(): ProgressBar
    fun loggedUser(): TextView
    fun timer(): TextView
    fun showUserMsg(userMsg: String)
}