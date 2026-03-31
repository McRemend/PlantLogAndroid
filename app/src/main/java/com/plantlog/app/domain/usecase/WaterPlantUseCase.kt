package com.plantlog.app.domain.usecase

import com.plantlog.app.data.repository.PlantRepository
import javax.inject.Inject

/**
 * 浇水用例
 */
class WaterPlantUseCase @Inject constructor(
    private val repository: PlantRepository
) {
    suspend operator fun invoke(plantId: Long) = 
        repository.waterPlant(plantId)
}
