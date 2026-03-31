package com.plantlog.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Application 入口类
 */
@HiltAndroidApp
class PlantLogApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}
