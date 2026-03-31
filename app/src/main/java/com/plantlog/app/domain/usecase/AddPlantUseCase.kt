package com.plantlog.app.domain.usecase

import com.plantlog.app.data.repository.PlantRepository
import com.plantlog.app.domain.mapper.toEntity
import com.plantlog.app.domain.model.Plant
import javax.inject.Inject

/**
 * 添加植物用例
 */
class AddPlantUseCase @Inject constructor(
    private val repository: PlantRepository
) {
    suspend operator fun invoke(plant: Plant): Long = 
        repository.insertPlant(plant.toEntity())
}
