package com.ssrdi.co.id.myradboox

import android.app.Application
import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import timber.log.Timber
import java.util.*

class RadbooxApp : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(CrashlyticsTree())
        }
    }
}


class CrashlyticsTree() : Timber.Tree() {

    override fun log(priority: Int, tag: String?, message: String, throwable: Throwable?) {

        if (priority == Log.VERBOSE || priority == Log.DEBUG) {
            return
        }

        val t = throwable ?: Exception(message)

        //15:47:28.123 - ClassName: DEBUG/My Message here
        var msg = Date().toString()
        msg += " - "
        if (tag != null) msg += "$tag: "
        msg += print(priority) + "/"
        msg += message

        if (t != null) {
            //add \nStack:...
            val elements = t.stackTrace
            msg += "\nStack:"
            for (element in elements) {
                msg += "\n\t" + element
            }
        }
        FirebaseCrashlytics.getInstance().setCustomKey("priority", priority)
        FirebaseCrashlytics.getInstance().setCustomKey("tag", tag ?: "")
        FirebaseCrashlytics.getInstance().setCustomKey("message", message)
        FirebaseCrashlytics.getInstance().log(tag + ": " + print(priority) + "/" + msg)
        FirebaseCrashlytics.getInstance().recordException(t)
    }

    private fun print(priority: Int): String {
        return when (priority) {
            Log.VERBOSE -> "TRACE"
            Log.DEBUG -> "DEBUG"
            Log.WARN -> "WARN "
            Log.ERROR -> "ERROR"
            else -> "? ($priority)"
        }
    }
}