package com.plantlog.app.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

/**
 * 植物实体 - Room 数据库表
 * @param id 唯一标识符
 * @param name 植物名称
 * @param scientificName 学名（可选）
 * @param nickname 用户自定义昵称
 * @param imageUrl 图片 URI
 * @param acquiredDate 获取日期
 * @param waterInterval 浇水间隔（天）
 * @param lastWatered 上次浇水时间
 * @param notes 备注
 * @param isFavorite 是否收藏
 */
@Entity(tableName = "plants")
data class PlantEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    
    val name: String,
    val scientificName: String? = null,
    val nickname: String? = null,
    val imageUrl: String? = null,
    val acquiredDate: Long = System.currentTimeMillis(),
    val waterInterval: Int = 7, // 默认 7 天浇一次
    val lastWatered: Long = System.currentTimeMillis(),
    val notes: String? = null,
    val isFavorite: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
