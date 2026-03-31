package com.plantlog.app.domain.model

import com.plantlog.app.data.local.CareType

/**
 * 领域模型 - 护理记录
 */
data class CareRecord(
    val id: Long = 0,
    val plantId: Long,
    val careType: CareType,
    val date: Long = System.currentTimeMillis(),
    val notes: String? = null
)
