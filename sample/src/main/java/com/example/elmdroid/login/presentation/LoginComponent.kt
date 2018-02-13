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
        is EmailChanged ->      prevState.copy(email = msg.email, msgText = "").updateLogin().noCmd()
        is PasswordChanged ->   prevState.copy(password = msg.password, msgText = "").updateLogin().noCmd()
        LoginClicked ->      prevState.copy(loadingVisible = true, loginEnabled = false, msgText = "") withCmd LoginAction(prevState.email, prevState.password)
        is LoginSuccess ->      prevState.copy(loadingVisible = false, msgText = "Login Successful, welcome ${msg.username}", email = "", password = "").updateLogin().noCmd()
        is LoggedUserChanged -> prevState.copy(loggedUsername = msg.username, loggedTimer = 0).noCmd()
        Tick ->                 prevState.copy(loggedTimer = (prevState.loggedTimer + 1)).noCmd()
    }

    override fun call(cmd: LoginCmd): Single<LoginMsg> = when(cmd) {
        is LoginAction -> loginTask(cmd.email, cmd.password)
    }

    override fun subscriptions(): List<Sub<LoginState, LoginMsg>> = listOf(UserSubscription(), CounterSubscription())

    private fun LoginState.updateLogin() = this.copy(loginEnabled = (email.isNotBlank() && password.isNotBlank()))
}


// Tasks
fun loginTask(email: String, password: String): Single<LoginMsg> {
    pause(1000)
    UserRepository().setUser(email)
    return Single.just(LoginSuccess("Mr/Mrs $email"))
}

// Subscriptions
class UserSubscription : StatelessSub<LoginState, LoginMsg>() {
    override fun invoke(): Observable<LoginMsg> = UserRepository().getUser().map {
        pause(2000)
        LoggedUserChanged(it.username)
    }
}

class CounterSubscription : StatefulSub<LoginState, LoginMsg>() {
    override fun invoke(state: LoginState): Observable<LoginMsg> = when {
        state.loggedUsername.isNotBlank() -> Observable.interval(1, TimeUnit.SECONDS).map { Tick }
        else -> Observable.empty()
    }
    override fun isDistinct(s1: LoginState, s2: LoginState) = s1.loggedUsername != s2.loggedUsername
}


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
//data class ErrorMsg(val error: Throwable) : LoginMsg()
data class LoginSuccess(val username: String) : LoginMsg()
data class LoggedUserChanged(val username: String) : LoginMsg()
object Tick : LoginMsg()
object LoginClicked: LoginMsg()

// Cmd
sealed class LoginCmd : Cmd
data class LoginAction(val email: String, val password: String) : LoginCmd()