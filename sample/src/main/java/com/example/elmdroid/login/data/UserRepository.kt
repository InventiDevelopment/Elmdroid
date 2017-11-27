package com.example.elmdroid.login.data

import com.example.elmdroid.common.pause
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Observable

class UserRepository {
    fun getUser(): Observable<User> = user

    fun setUser(username: String) {
        user.accept(User(username))
    }

    companion object {
        private var user: BehaviorRelay<User> = BehaviorRelay.create()
    }
}

