package com.plantlog.app.domain.usecase

import com.plantlog.app.data.repository.PlantRepository
import com.plantlog.app.domain.mapper.toDomain
import com.plantlog.app.domain.model.Plant
import javax.inject.Inject

/**
 * 根据 ID 获取植物用例
 */
class GetPlantByIdUseCase @Inject constructor(
    private val repository: PlantRepository
) {
    suspend operator fun invoke(plantId: Long): Plant? = 
        repository.getPlantById(plantId)?.toDomain()
}
