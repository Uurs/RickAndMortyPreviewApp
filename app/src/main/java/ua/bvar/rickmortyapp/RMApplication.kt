package ua.bvar.rickmortyapp

import android.app.Application
import android.os.Build
import android.os.StrictMode
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class RMApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            enableStrictMode()
        }
    }

    private fun enableStrictMode() {
        StrictMode.setVmPolicy(
            StrictMode.VmPolicy.Builder()
                .apply {
                    detectLeakedSqlLiteObjects()
                    detectActivityLeaks()
                    detectLeakedClosableObjects()
                    detectLeakedRegistrationObjects()
                    detectFileUriExposure()
                    detectContentUriWithoutPermission()
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        detectCredentialProtectedWhileLocked()
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        detectUnsafeIntentLaunch()
                        detectIncorrectContextUse()
                    }
                }
                .build()
        )
        StrictMode.setThreadPolicy(
            StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .build()
        )
    }
}