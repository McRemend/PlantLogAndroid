package com.plantlog.app.domain.usecase

import com.plantlog.app.data.repository.PlantRepository
import com.plantlog.app.domain.mapper.toDomain
import com.plantlog.app.domain.model.Plant
import javax.inject.Inject

/**
 * 获取需要浇水的植物用例
 */
class GetPlantsNeedWaterUseCase @Inject constructor(
    private val repository: PlantRepository
) {
    suspend operator fun invoke(): List<Plant> = 
        repository.getPlantsNeedWater().map { it.toDomain() }
}
