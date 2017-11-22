package com.example.elmdroid.login

import cz.inventi.elmdroid.*
import io.reactivex.Single

/**
 * Created by tomas.valenta on 11/21/2017.
 */
class LoginComponent : Component<LoginState, LoginMsg, LoginCmd> {


    override fun initState(): LoginState = LoginState("", "", false,false, "Anon", "")

    override fun update(msg: LoginMsg, ps: LoginState): Pair<LoginState, LoginCmd> = when(msg) {
        is EmailChanged -> ps.copy(email = msg.email, userMsg = "").updateLoginEnabled() to None
        is PasswordChanged -> ps.copy(password = msg.password, userMsg = "").updateLoginEnabled() to None
        is LoginClicked -> ps.copy(loadingVisible = true, loginEnabled = false, userMsg = "") to LoginAction(ps.email, ps.password)
        is LoginSuccess -> ps.copy(loadingVisible = false, userMsg = "Login Successful, welcome ${msg.username}", email = "", password = "").updateLoginEnabled() to None
        is ErrorMsg -> ps.copy(loadingVisible = false, userMsg = "Login Failed: ${msg.error.message}").updateLoginEnabled() to None
        is Idle -> ps to None
    }

    override fun call(cmd: LoginCmd): Single<LoginMsg> = when(cmd) {
        is None -> Single.just(Idle)
        is LoginAction -> loginUseCase(cmd.email, cmd.password)
    }

    private fun LoginState.updateLoginEnabled() = this.copy(loginEnabled = (email.isNotBlank() && password.isNotBlank()))

    override fun handleCmdError(error: Throwable, cmd: LoginCmd): LoginMsg = ErrorMsg(error, cmd)
    override fun filterCmd(cmd: LoginCmd): Boolean = cmd !is None
}

// State
data class LoginState(val email: String, val password: String, val loginEnabled: Boolean, val loadingVisible: Boolean, val loggedUsername: String, val userMsg: String) : State

// Msg
sealed class LoginMsg : Msg
data class EmailChanged(val email: String): LoginMsg()
data class PasswordChanged(val password: String): LoginMsg()
data class ErrorMsg(val error: Throwable, val cmd: LoginCmd) : LoginMsg()
data class LoginSuccess(val username: String) : LoginMsg()
object LoginClicked: LoginMsg()
object Idle : LoginMsg()

// Cmd
sealed class LoginCmd : Cmd
object None : LoginCmd()
data class LoginAction(val email: String, val password: String) : LoginCmd()