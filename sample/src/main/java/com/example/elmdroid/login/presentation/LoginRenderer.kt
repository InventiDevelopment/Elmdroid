package com.example.elmdroid.login.presentation

import android.view.View
import cz.inventi.elmdroid.BaseViewRenderer


class LoginRenderer(view: LoginView?) : BaseViewRenderer<LoginView, LoginState>(view) {

    override fun LoginView.render(state: LoginState) {

        state from { it.loggedUsername } into {
            loggedUser().text = it
        } from { it.loginEnabled } into {
            loginButton().isEnabled
        } from { it.loadingVisible } into {
            if (it){
                email().isEnabled = false
                password().isEnabled = false
                progressBar().visibility = View.VISIBLE
            } else {
                email().isEnabled = true
                password().isEnabled = true
                progressBar().visibility = View.GONE
            }
        } from { it.msgText } into {
            if (it.isNotBlank()) {
                showUserMsg(state.msgText)
            }
        } from { it.loggedTimer } into {
            timer().text = "$it"
        }
    }
}