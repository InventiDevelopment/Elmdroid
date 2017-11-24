package com.example.elmdroid.login.presentation

import com.example.elmdroid.common.pause
import com.example.elmdroid.login.data.UserRepository
import cz.inventi.elmdroid.*
import io.reactivex.Observable
import io.reactivex.Single
import java.util.concurrent.TimeUnit

class LoginComponent : Component<LoginState, LoginMsg, LoginCmd> {


    override fun initState(): LoginState = LoginState("", "", false, false, "", "", 0)

    override fun update(msg: LoginMsg, prevState: LoginState): Pair<LoginState, LoginCmd?> = when(msg) {
        is EmailChanged ->      prevState.copy(email = msg.email, msgText = "").updateLogin().withoutCmd()
        is PasswordChanged ->   prevState.copy(password = msg.password, msgText = "").updateLogin().withoutCmd()
        is LoginClicked ->      prevState.copy(loadingVisible = true, loginEnabled = false, msgText = "") withCmd LoginAction(prevState.email, prevState.password)
        is LoginSuccess ->      prevState.copy(loadingVisible = false, msgText = "Login Successful, welcome ${msg.username}", email = "", password = "").updateLogin().withoutCmd()
        is LoggedUserChanged -> prevState.copy(loggedUsername = msg.username).withoutCmd()
        Tick ->                 prevState.copy(loggedTimer = (prevState.loggedTimer + 1)).withoutCmd()
        is ErrorMsg ->          prevState.copy(loadingVisible = false, msgText = "Login Failed: ${msg.error.message}").withoutCmd()
    }

    override fun call(cmd: LoginCmd): Single<LoginMsg> = when(cmd) {
        is LoginAction -> loginTask(cmd.email, cmd.password)
    }

    override fun subscriptions(): Observable<LoginMsg> = Observable.merge(userSubscription(), counterSubscription())

    override fun onError(error: Throwable): LoginMsg = ErrorMsg(error)

    private fun LoginState.updateLogin() = this.copy(loginEnabled = (email.isNotBlank() && password.isNotBlank()))
}


// Tasks
fun loginTask(email: String, password: String): Single<LoginMsg> {
    pause(1000)
    UserRepository().setUser(email)
    return Single.just(LoginSuccess("Mr/Mrs $email"))
}

// Subscriptions
fun userSubscription(): Observable<LoginMsg> = UserRepository().getUser().map {
    pause(2000)
    LoggedUserChanged(it.username)
}

fun counterSubscription(): Observable<LoginMsg> = Observable.interval(1, TimeUnit.SECONDS).map { Tick }


// State
data class LoginState(
        val email: String,
        val password: String,
        val loginEnabled: Boolean,
        val loadingVisible: Boolean,
        val loggedUsername: String,
        val msgText: String,
        val loggedTimer: Int
) : State

// Msg
sealed class LoginMsg : Msg
data class EmailChanged(val email: String): LoginMsg()
data class PasswordChanged(val password: String): LoginMsg()
data class ErrorMsg(val error: Throwable) : LoginMsg()
data class LoginSuccess(val username: String) : LoginMsg()
data class LoggedUserChanged(val username: String) : LoginMsg()
object Tick : LoginMsg()
object LoginClicked: LoginMsg()

// Cmd
sealed class LoginCmd : Cmd
data class LoginAction(val email: String, val password: String) : LoginCmd()