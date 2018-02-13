package com.example.elmdroid.login.presentation

import cz.inventi.elmdroid.ElmViewModel
import cz.inventi.elmdroid.LogLevel

/**
 * Created by tomas.valenta on 11/27/2017.
 */
class LoginViewModel : ElmViewModel<LoginState, LoginMsg, LoginCmd>(LoginComponent(), LogLevel.FULL) {
    // any additional component unrelated code
}