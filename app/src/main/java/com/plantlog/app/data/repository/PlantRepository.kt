package com.plantlog.app.data.repository

import com.plantlog.app.data.local.CareRecordDao
import com.plantlog.app.data.local.CareRecordEntity
import com.plantlog.app.data.local.CareType
import com.plantlog.app.data.local.PlantDao
import com.plantlog.app.data.local.PlantEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 植物数据仓库 - 单一数据源
 */
@Singleton
class PlantRepository @Inject constructor(
    private val plantDao: PlantDao,
    private val careRecordDao: CareRecordDao
) {
    
    // ============ Plant 操作 ============
    
    fun getAllPlants(): Flow<List<PlantEntity>> = plantDao.getAllPlants()
    
    fun getFavoritePlants(): Flow<List<PlantEntity>> = plantDao.getFavoritePlants()
    
    suspend fun getPlantById(plantId: Long): PlantEntity? = plantDao.getPlantById(plantId)
    
    suspend fun getPlantsNeedWater(): List<PlantEntity> = plantDao.getPlantsNeedWater()
    
    suspend fun insertPlant(plant: PlantEntity): Long = plantDao.insert(plant)
    
    suspend fun updatePlant(plant: PlantEntity) = plantDao.update(plant)
    
    suspend fun deletePlant(plant: PlantEntity) = plantDao.delete(plant)
    
    suspend fun waterPlant(plantId: Long) {
        plantDao.updateLastWatered(plantId)
        // 同时添加护理记录
        val record = CareRecordEntity(
            plantId = plantId,
            careType = CareType.WATER
        )
        careRecordDao.insert(record)
    }
    
    // ============ CareRecord 操作 ============
    
    fun getCareRecords(plantId: Long): Flow<List<CareRecordEntity>> = 
        careRecordDao.getRecordsByPlantId(plantId)
    
    suspend fun addCareRecord(record: CareRecordEntity): Long = 
        careRecordDao.insert(record)
    
    suspend fun getLastWaterDate(plantId: Long): Long? =
        careRecordDao.getLastCareRecord(plantId, CareType.WATER)?.date
}
