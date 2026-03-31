package com.plantlog.app.domain.model

import java.util.Date

/**
 * 领域模型 - 植物
 * 使用数据类，便于在层之间传递
 */
data class Plant(
    val id: Long = 0,
    val name: String,
    val scientificName: String? = null,
    val nickname: String? = null,
    val imageUrl: String? = null,
    val acquiredDate: Long = System.currentTimeMillis(),
    val waterInterval: Int = 7,
    val lastWatered: Long = System.currentTimeMillis(),
    val notes: String? = null,
    val isFavorite: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    /**
     * 计算下次浇水日期
     */
    fun getNextWaterDate(): Long = lastWatered + (waterInterval.toLong() * 24 * 60 * 60 * 1000)
    
    /**
     * 判断是否需要浇水
     */
    fun needsWater(currentTime: Long = System.currentTimeMillis()): Boolean = 
        currentTime >= getNextWaterDate()
    
    /**
     * 距离下次浇水剩余天数
     */
    fun daysUntilNextWater(currentTime: Long = System.currentTimeMillis()): Int {
        val diff = getNextWaterDate() - currentTime
        return (diff / (24 * 60 * 60 * 1000)).toInt().coerceAtLeast(0)
    }
}
