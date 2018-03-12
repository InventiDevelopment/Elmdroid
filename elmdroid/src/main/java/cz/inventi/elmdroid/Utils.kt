package cz.inventi.elmdroid

import android.util.Log
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

internal fun <T> Observable<T>.subscribeNewObserveMain(): Observable<T> {
    return this.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
}

internal fun log(
        logLevel: LogLevel,
        minLogLevel: LogLevel,
        tag: String,
        logMsg: String
) {
    if (minLogLevel.logLevelIndex <= logLevel.logLevelIndex) {
        Log.d(tag, logMsg)
    }
}