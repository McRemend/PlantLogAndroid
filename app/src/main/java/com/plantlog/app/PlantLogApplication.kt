package com.plantlog.app

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Application 入口类
 */
@HiltAndroidApp
class PlantLogApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
    }
}

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    
    @Provides
    @Singleton
    fun provideContext(application: PlantLogApplication): Context = application
}
