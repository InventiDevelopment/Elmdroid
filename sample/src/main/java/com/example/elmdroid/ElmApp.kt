package com.example.elmdroid

import android.app.Application
import cz.inventi.elmdroid.LogLevel
import cz.inventi.elmdroid.RuntimeFactory


class ElmApp : Application() {
    override fun onCreate() {
        super.onCreate()
        RuntimeFactory.defaultLogLevel = if (BuildConfig.DEBUG)  LogLevel.FULL else LogLevel.NONE
    }
}