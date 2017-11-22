package com.example.elmdroid.login

import io.reactivex.Single

/**
 * Created by tomas.valenta on 11/22/2017.
 */
fun loginUseCase(email: String, password: String): Single<LoginMsg> {
    try {
        Thread.sleep(3000)
    } catch (e: InterruptedException) {
        e.printStackTrace()
        throw IllegalAccessException("Something went wrong!")
    }
    return Single.just(LoginSuccess("Mr/Mrs $email"))

}