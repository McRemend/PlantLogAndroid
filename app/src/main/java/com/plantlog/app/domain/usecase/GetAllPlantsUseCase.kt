package com.plantlog.app.domain.usecase

import com.plantlog.app.data.repository.PlantRepository
import com.plantlog.app.domain.mapper.toDomain
import com.plantlog.app.domain.model.Plant
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * 获取所有植物用例
 */
class GetAllPlantsUseCase @Inject constructor(
    private val repository: PlantRepository
) {
    operator fun invoke(): Flow<List<Plant>> = 
        repository.getAllPlants().map { entities -> entities.map { it.toDomain() } }
}
