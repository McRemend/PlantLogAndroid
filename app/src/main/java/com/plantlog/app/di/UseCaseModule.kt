package com.plantlog.app.di

import com.plantlog.app.data.repository.PlantRepository
import com.plantlog.app.domain.usecase.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt 模块 - 提供 UseCase 依赖
 */
@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    
    @Provides
    @Singleton
    fun provideGetAllPlantsUseCase(repository: PlantRepository): GetAllPlantsUseCase =
        GetAllPlantsUseCase(repository)
    
    @Provides
    @Singleton
    fun provideGetPlantByIdUseCase(repository: PlantRepository): GetPlantByIdUseCase =
        GetPlantByIdUseCase(repository)
    
    @Provides
    @Singleton
    fun provideAddPlantUseCase(repository: PlantRepository): AddPlantUseCase =
        AddPlantUseCase(repository)
    
    @Provides
    @Singleton
    fun provideWaterPlantUseCase(repository: PlantRepository): WaterPlantUseCase =
        WaterPlantUseCase(repository)
    
    @Provides
    @Singleton
    fun provideGetPlantsNeedWaterUseCase(repository: PlantRepository): GetPlantsNeedWaterUseCase =
        GetPlantsNeedWaterUseCase(repository)
}
