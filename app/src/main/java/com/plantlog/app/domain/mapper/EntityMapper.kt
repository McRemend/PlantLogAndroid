package com.plantlog.app.domain.mapper

import com.plantlog.app.data.local.CareRecordEntity
import com.plantlog.app.data.local.PlantEntity
import com.plantlog.app.domain.model.CareRecord
import com.plantlog.app.domain.model.Plant

/**
 * 实体与领域模型之间的映射扩展函数
 */

// Plant 映射
fun PlantEntity.toDomain(): Plant = Plant(
    id = this.id,
    name = this.name,
    scientificName = this.scientificName,
    nickname = this.nickname,
    imageUrl = this.imageUrl,
    acquiredDate = this.acquiredDate,
    waterInterval = this.waterInterval,
    lastWatered = this.lastWatered,
    notes = this.notes,
    isFavorite = this.isFavorite,
    createdAt = this.createdAt,
    updatedAt = this.updatedAt
)

fun Plant.toEntity(): PlantEntity = PlantEntity(
    id = this.id,
    name = this.name,
    scientificName = this.scientificName,
    nickname = this.nickname,
    imageUrl = this.imageUrl,
    acquiredDate = this.acquiredDate,
    waterInterval = this.waterInterval,
    lastWatered = this.lastWatered,
    notes = this.notes,
    isFavorite = this.isFavorite,
    createdAt = this.createdAt,
    updatedAt = this.updatedAt
)

// CareRecord 映射
fun CareRecordEntity.toDomain(): CareRecord = CareRecord(
    id = this.id,
    plantId = this.plantId,
    careType = this.careType,
    date = this.date,
    notes = this.notes
)

fun CareRecord.toEntity(): CareRecordEntity = CareRecordEntity(
    id = this.id,
    plantId = this.plantId,
    careType = this.careType,
    date = this.date,
    notes = this.notes
)
