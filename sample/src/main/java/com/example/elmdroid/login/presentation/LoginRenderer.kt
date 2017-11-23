package com.example.elmdroid.login.presentation

import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.Observer
import android.view.View


class LoginRenderer(private val view: LoginView) : Observer<LoginState>, LifecycleObserver {
    override fun onChanged(state: LoginState?) {
        state?.apply {
            view.loggedUser().text = loggedUsername
            view.loginButton().isEnabled = loginEnabled
            if (loadingVisible){
                view.email().isEnabled = false
                view.password().isEnabled = false
                view.progressBar().visibility = View.VISIBLE
            } else {
                view.email().isEnabled = true
                view.password().isEnabled = true
                view.progressBar().visibility = View.GONE
            }

            if (view.email().text.toString() != email) {
                view.email().setText(email)
            }
            if (view.password().text.toString() != password) {
                view.password().setText(password)
            }

            if (msgText.isNotBlank()) {
                view.showUserMsg(msgText)
            }

            view.timer().text = "$loggedTimer"
        }
    }
}