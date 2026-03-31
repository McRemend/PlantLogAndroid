package com.plantlog.app.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Room 数据库定义
 */
@Database(
    entities = [PlantEntity::class, CareRecordEntity::class],
    version = 1,
    exportSchema = false
)
abstract class PlantLogDatabase : RoomDatabase() {
    
    abstract fun plantDao(): PlantDao
    abstract fun careRecordDao(): CareRecordDao
    
    companion object {
        @Volatile
        private var INSTANCE: PlantLogDatabase? = null
        
        fun getDatabase(context: Context): PlantLogDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PlantLogDatabase::class.java,
                    "plantlog_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
