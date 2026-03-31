package com.plantlog.app.di

import android.content.Context
import com.plantlog.app.PlantLogApplication
import com.plantlog.app.data.local.CareRecordDao
import com.plantlog.app.data.local.PlantDao
import com.plantlog.app.data.local.PlantLogDatabase
import com.plantlog.app.data.repository.PlantRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt 模块 - 提供数据库相关依赖
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
    @Provides
    @Singleton
    fun provideContext(application: PlantLogApplication): Context =
        application
    
    @Provides
    @Singleton
    fun provideDatabase(context: Context): PlantLogDatabase =
        PlantLogDatabase.getDatabase(context)
    
    @Provides
    @Singleton
    fun providePlantDao(database: PlantLogDatabase): PlantDao =
        database.plantDao()
    
    @Provides
    @Singleton
    fun provideCareRecordDao(database: PlantLogDatabase): CareRecordDao =
        database.careRecordDao()
    
    @Provides
    @Singleton
    fun providePlantRepository(
        plantDao: PlantDao,
        careRecordDao: CareRecordDao
    ): PlantRepository =
        PlantRepository(plantDao, careRecordDao)
}
