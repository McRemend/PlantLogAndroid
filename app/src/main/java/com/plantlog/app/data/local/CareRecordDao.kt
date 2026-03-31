package com.plantlog.app.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 * 护理记录数据访问对象
 */
@Dao
interface CareRecordDao {
    
    @Query("SELECT * FROM care_records WHERE plantId = :plantId ORDER BY date DESC")
    fun getRecordsByPlantId(plantId: Long): Flow<List<CareRecordEntity>>
    
    @Query("SELECT * FROM care_records WHERE id = :recordId")
    suspend fun getRecordById(recordId: Long): CareRecordEntity?
    
    @Query("SELECT * FROM care_records WHERE plantId = :plantId AND careType = :careType ORDER BY date DESC LIMIT 1")
    suspend fun getLastCareRecord(plantId: Long, careType: CareType): CareRecordEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(record: CareRecordEntity): Long
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(records: List<CareRecordEntity>)
    
    @Delete
    suspend fun delete(record: CareRecordEntity)
    
    @Query("DELETE FROM care_records WHERE plantId = :plantId")
    suspend fun deleteAllForPlant(plantId: Long)
}
