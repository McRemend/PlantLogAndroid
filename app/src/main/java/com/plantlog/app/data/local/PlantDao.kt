package com.plantlog.app.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 * 植物数据访问对象
 */
@Dao
interface PlantDao {
    
    @Query("SELECT * FROM plants ORDER BY updatedAt DESC")
    fun getAllPlants(): Flow<List<PlantEntity>>
    
    @Query("SELECT * FROM plants WHERE id = :plantId")
    suspend fun getPlantById(plantId: Long): PlantEntity?
    
    @Query("SELECT * FROM plants WHERE isFavorite = 1 ORDER BY updatedAt DESC")
    fun getFavoritePlants(): Flow<List<PlantEntity>>
    
    @Query("SELECT * FROM plants WHERE lastWatered + (waterInterval * 86400000) < :currentTime ORDER BY lastWatered ASC")
    suspend fun getPlantsNeedWater(currentTime: Long = System.currentTimeMillis()): List<PlantEntity>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(plant: PlantEntity): Long
    
    @Update
    suspend fun update(plant: PlantEntity)
    
    @Delete
    suspend fun delete(plant: PlantEntity)
    
    @Query("UPDATE plants SET lastWatered = :timestamp, updatedAt = :timestamp WHERE id = :plantId")
    suspend fun updateLastWatered(plantId: Long, timestamp: Long = System.currentTimeMillis())
}
