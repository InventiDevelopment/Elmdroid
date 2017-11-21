package com.example.elmdroid.login

import cz.inventi.elmdroid.*
import io.reactivex.Single

/**
 * Created by tomas.valenta on 11/21/2017.
 */
class LoginComponent : Component<LoginState, LoginMsg, LoginCmd> {


    override fun initState(): LoginState = LoginState("", "", false,false, "Anon")

    override fun update(msg: LoginMsg, ps: LoginState): Pair<LoginState, LoginCmd> = when(msg) {
        is EmailChanged -> ps.copy(email = msg.email).updateLoginEnabled() to None
        is PasswordChanged -> ps.copy(password = msg.password).updateLoginEnabled() to None
        is LoginClicked -> ps.copy(loadingVisible = true, loginEnabled = false) to LoginAction(ps.email, ps.password)
        is Idle -> ps to None
    }

    override fun call(cmd: LoginCmd): Single<LoginMsg> = when(cmd) {
        is None -> Single.just(Idle)
        is LoginAction -> Single.just(Idle) // todo change
    }

    private fun LoginState.updateLoginEnabled() = this.copy(loginEnabled = (email.isNotBlank() && password.isNotBlank()))

}

// State
data class LoginState(val email: String, val password: String, val loginEnabled: Boolean, val loadingVisible: Boolean, val loggedUsername: String) : State

// Msg
sealed class LoginMsg : Msg
data class EmailChanged(val email: String): LoginMsg()
data class PasswordChanged(val password: String): LoginMsg()
object LoginClicked: LoginMsg()
object Idle : LoginMsg()

// Cmd
sealed class LoginCmd : Cmd
object None : LoginCmd()
data class LoginAction(val email: String, val password: String) : LoginCmd()