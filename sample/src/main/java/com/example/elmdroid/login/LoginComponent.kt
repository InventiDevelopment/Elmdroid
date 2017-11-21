package com.example.elmdroid.login

import cz.inventi.elmdroid.Component
import cz.inventi.elmdroid.Msg
import cz.inventi.elmdroid.State

/**
 * Created by tomas.valenta on 11/21/2017.
 */
class LoginComponent : Component<LoginState, LoginMsg> {
    override fun initState(): LoginState = LoginState("", "", false,false, "Anon")

    override fun update(msg: LoginMsg, prevState: LoginState): LoginState = when(msg) {
        is EmailChanged -> prevState.copy(email = msg.email).updateLoginEnabled()
        is PasswordChanged -> prevState.copy(password = msg.password).updateLoginEnabled()
        is LoginClicked -> prevState.copy(loadingVisible = true, loginEnabled = false)
    }

    private fun LoginState.updateLoginEnabled() = this.copy(loginEnabled = (email.isNotBlank() && password.isNotBlank()))

}

data class LoginState(val email: String, val password: String, val loginEnabled: Boolean, val loadingVisible: Boolean, val loggedUsername: String) : State

sealed class LoginMsg : Msg
data class EmailChanged(val email: String): LoginMsg()
data class PasswordChanged(val password: String): LoginMsg()
object LoginClicked: LoginMsg()