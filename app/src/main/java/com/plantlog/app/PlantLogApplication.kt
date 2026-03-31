package com.plantlog.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Application 入口类
 * 使用 Hilt 进行依赖注入
 */
@HiltAndroidApp
class PlantLogApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // 应用级别初始化可在此进行
    }
}
