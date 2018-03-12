package com.example.elmdroid

import android.app.Application
import cz.inventi.elmdroid.Elmdroid
import cz.inventi.elmdroid.LogLevel


class ElmApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Elmdroid.defaultLogLevel = if (BuildConfig.DEBUG) LogLevel.FULL else LogLevel.NONE
    }
}