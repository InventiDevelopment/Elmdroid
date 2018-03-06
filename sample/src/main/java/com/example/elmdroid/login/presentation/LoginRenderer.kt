package com.example.elmdroid.login.presentation

import android.view.View
import cz.inventi.elmdroid.ViewRenderer


class LoginRenderer(view: LoginView?) : ViewRenderer<LoginView, LoginState>(view) {

    override fun LoginView.render(state: LoginState) {

        state applyProperty { it.loggedUsername } into {
            loggedUser().text = it
        } applyProperty { it.loginEnabled } into {
            loginButton().isEnabled
        } applyProperty { it.loadingVisible } into {
            if (it){
                email().isEnabled = false
                password().isEnabled = false
                progressBar().visibility = View.VISIBLE
            } else {
                email().isEnabled = true
                password().isEnabled = true
                progressBar().visibility = View.GONE
            }
        } applyProperty { it.msgText } into {
            if (it.isNotBlank()) {
                showUserMsg(state.msgText)
            }
        } applyProperty { it.loggedTimer } into {
            timer().text = "$it"
        }
    }
}