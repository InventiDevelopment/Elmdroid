package com.example.elmdroid.login.presentation

import android.view.View
import com.example.elmdroid.common.ViewRenderer


class LoginRenderer(view: LoginView?) : ViewRenderer<LoginView, LoginState>(view) {

    override fun LoginView.render(state: LoginState) {
        loggedUser().text = state.loggedUsername
        loginButton().isEnabled = state.loginEnabled
        if (state.loadingVisible){
            email().isEnabled = false
            password().isEnabled = false
            progressBar().visibility = View.VISIBLE
        } else {
            email().isEnabled = true
            password().isEnabled = true
            progressBar().visibility = View.GONE
        }

        if (email().text.toString() != state.email) {
            email().setText(state.email)
        }
        if (password().text.toString() != state.password) {
            password().setText(state.password)
        }

        if (state.msgText.isNotBlank()) {
            showUserMsg(state.msgText)
        }

        timer().text = "${state.loggedTimer}"
    }
}