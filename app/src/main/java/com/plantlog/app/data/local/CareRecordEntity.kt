package com.plantlog.app.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

/**
 * 护理记录实体 - 记录每次浇水、施肥等护理操作
 * @param id 唯一标识符
 * @param plantId 关联的植物 ID
 * @param careType 护理类型（WATER, FERTILIZE, PRUNE, REPOT）
 * @param date 护理日期
 * @param notes 备注
 */
@Entity(
    tableName = "care_records",
    foreignKeys = [
        ForeignKey(
            entity = PlantEntity::class,
            parentColumns = ["id"],
            childColumns = ["plantId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["plantId"])]
)
data class CareRecordEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    
    val plantId: Long,
    val careType: CareType,
    val date: Long = System.currentTimeMillis(),
    val notes: String? = null
)

/**
 * 护理类型枚举
 */
enum class CareType {
    WATER,      // 浇水
    FERTILIZE,  // 施肥
    PRUNE,      // 修剪
    REPOT,      // 换盆
    PEST_CONTROL // 除虫
}
