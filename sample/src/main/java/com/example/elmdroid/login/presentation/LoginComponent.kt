package com.example.elmdroid.login.presentation

import com.example.elmdroid.common.pause
import com.example.elmdroid.login.data.UserRepository
import cz.inventi.elmdroid.*
import io.reactivex.Observable
import io.reactivex.Single
import java.util.concurrent.TimeUnit

/**
 * Created by tomas.valenta on 11/21/2017.
 */
class LoginComponent : Component<LoginState, LoginMsg, LoginCmd> {


    override fun initState(): LoginState = LoginState("", "", false, false, "Anon", "")

    override fun update(msg: LoginMsg, ps: LoginState): Pair<LoginState, LoginCmd> = when(msg) {
        is EmailChanged -> ps.copy(email = msg.email, msgText = "").updateLoginEnabled() to None
        is PasswordChanged -> ps.copy(password = msg.password, msgText = "").updateLoginEnabled() to None
        is LoginClicked -> ps.copy(loadingVisible = true, loginEnabled = false, msgText = "") to LoginAction(ps.email, ps.password)
        is LoginSuccess -> ps.copy(loadingVisible = false, msgText = "Login Successful, welcome ${msg.username}", email = "", password = "").updateLoginEnabled() to None
        is LoggedUserChanged -> ps.copy(loggedUsername = msg.username) to None
        is ErrorMsg -> ps.copy(loadingVisible = false, msgText = "Login Failed: ${msg.error.message}") to None
        is Idle -> ps to None
    }

    override fun call(cmd: LoginCmd): Single<LoginMsg> = when(cmd) {
        is None -> Single.just(Idle)
        is LoginAction -> loginTask(cmd.email, cmd.password)
    }

    override fun subscriptions(state: LoginState) = when {
        state.loggedUsername.isNotBlank() -> Observable.merge(userSubscription(), counterSubscription())
        else -> userSubscription()
    }

    override fun onError(error: Throwable): LoginMsg = ErrorMsg(error)
    override fun filterCmd(cmd: LoginCmd): Boolean = cmd !is None

    private fun LoginState.updateLoginEnabled() = this.copy(loginEnabled = (email.isNotBlank() && password.isNotBlank()))
}


// Tasks
fun loginTask(email: String, password: String): Single<LoginMsg> {
    pause(2000)
    UserRepository().setUser(email)
    return Single.just(LoginSuccess("Mr/Mrs $email"))
}

// Subscriptions
fun userSubscription(): Observable<LoginMsg> = UserRepository().getUser().map {
    pause(2000)
    LoggedUserChanged(it.username)
}

fun counterSubscription(): Observable<LoginMsg> = Observable.timer(1, TimeUnit.SECONDS).map { Tick }


// State
data class LoginState(val email: String, val password: String, val loginEnabled: Boolean, val loadingVisible: Boolean, val loggedUsername: String, val msgText: String) : State

// Msg
sealed class LoginMsg : Msg
data class EmailChanged(val email: String): LoginMsg()
data class PasswordChanged(val password: String): LoginMsg()
data class ErrorMsg(val error: Throwable) : LoginMsg()
data class LoginSuccess(val username: String) : LoginMsg()
data class LoggedUserChanged(val username: String) : LoginMsg()
object Tick : LoginMsg()
object LoginClicked: LoginMsg()
object Idle : LoginMsg()

// Cmd
sealed class LoginCmd : Cmd
object None : LoginCmd()
data class LoginAction(val email: String, val password: String) : LoginCmd()