package com.example.elmdroid.login.presentation

import android.view.View
import net.semanticer.renderit.BaseStateRenderer


class LoginRenderer(view: LoginView?) : BaseStateRenderer<LoginView, LoginState>(view) {

    override fun LoginView.render(state: LoginState) {

        state from { it.loggedUsername } into {
            loggedUser().text = it
        } from { it.loginEnabled } into {
            loginButton().isEnabled = it
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
        } from { it.email } into {
            if (it.isEmpty()) email().setText("")
        } from { it.password} into {
            if (it.isEmpty()) password().setText("")
        } from { it.msgText } into {
            if (it.isNotBlank()) {
                showUserMsg(state.msgText)
            }
        } from { it.loggedTimer } into {
            timer().text = "$it"
        }
    }
}