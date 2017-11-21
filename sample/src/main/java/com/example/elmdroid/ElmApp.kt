package com.example.elmdroid

import android.app.Application
import timber.log.Timber.DebugTree
import timber.log.Timber



/**
 * Created by tomas.valenta on 11/21/2017.
 */
class ElmApp : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }
    }

}