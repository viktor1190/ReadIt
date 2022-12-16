package com.victorvalencia.readdit

import android.app.Application
import android.content.Context
import androidx.startup.Initializer
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class ReadditApp: Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.v("[onCreate]") // timber should be initialized by this point
    }
}

class AppStartupInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        Timber.v("[create]")
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = listOf(
        TimberStartupInitializer::class.java
    )
}

class TimberStartupInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        if (isDebugBuild()) {
            Timber.plant(Timber.DebugTree())
        }
        Timber.v("[create]")
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()

    private fun isDebugBuild() = BuildConfig.DEBUG
}