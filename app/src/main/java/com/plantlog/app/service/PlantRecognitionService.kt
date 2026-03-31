package com.plantlog.app.service

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 植物识别服务 - 使用 ML Kit 进行图像识别
 * 注意：这是占位实现，实际使用需要配置 ML Kit 或调用第三方 API
 */
@Singleton
class PlantRecognitionService @Inject constructor(
    private val context: Context
) {
    
    private val labeler = ImageLabeling.getClient(
        ImageLabelerOptions.Builder()
            .setConfidenceThreshold(0.5f) // 置信度阈值 50%
            .build()
    )
    
    /**
     * 从图片 URI 识别植物
     * @param imageUri 图片 URI
     * @return 识别结果（植物名称、置信度）
     */
    suspend fun identifyPlantFromUri(imageUri: Uri): PlantRecognitionResult {
        return try {
            val image = InputImage.fromFilePath(context, imageUri)
            val labels = labeler.process(image).await()
            
            // 过滤与植物相关的标签
            val plantLabels = labels.filter { label ->
                isPlantRelated(label.text)
            }.sortedByDescending { it.confidence }
            
            if (plantLabels.isNotEmpty()) {
                PlantRecognitionResult(
                    success = true,
                    plantName = plantLabels.first().text,
                    confidence = plantLabels.first().confidence,
                    allLabels = plantLabels.map { Label(it.text, it.confidence) }
                )
            } else {
                PlantRecognitionResult(
                    success = false,
                    plantName = null,
                    confidence = 0f,
                    allLabels = labels.map { Label(it.text, it.confidence) },
                    errorMessage = "未识别到植物"
                )
            }
        } catch (e: Exception) {
            PlantRecognitionResult(
                success = false,
                plantName = null,
                confidence = 0f,
                allLabels = emptyList(),
                errorMessage = e.message ?: "识别失败"
            )
        }
    }
    
    /**
     * 从 Bitmap 识别植物
     */
    suspend fun identifyPlantFromBitmap(bitmap: Bitmap): PlantRecognitionResult {
        return try {
            val image = InputImage.fromBitmap(bitmap, 0)
            val labels = labeler.process(image).await()
            
            val plantLabels = labels.filter { label ->
                isPlantRelated(label.text)
            }.sortedByDescending { it.confidence }
            
            if (plantLabels.isNotEmpty()) {
                PlantRecognitionResult(
                    success = true,
                    plantName = plantLabels.first().text,
                    confidence = plantLabels.first().confidence,
                    allLabels = plantLabels.map { Label(it.text, it.confidence) }
                )
            } else {
                PlantRecognitionResult(
                    success = false,
                    plantName = null,
                    confidence = 0f,
                    allLabels = emptyList(),
                    errorMessage = "未识别到植物"
                )
            }
        } catch (e: Exception) {
            PlantRecognitionResult(
                success = false,
                plantName = null,
                confidence = 0f,
                allLabels = emptyList(),
                errorMessage = e.message ?: "识别失败"
            )
        }
    }
    
    /**
     * 判断标签是否与植物相关
     * 实际项目中可以扩展这个逻辑
     */
    private fun isPlantRelated(label: String): Boolean {
        val plantKeywords = listOf(
            "plant", "flower", "tree", "leaf", "succulent", "cactus",
            "fern", "palm", "orchid", "rose", "lily", "grass",
            "植物", "花", "树", "叶", "多肉", "仙人掌"
        )
        return plantKeywords.any { keyword ->
            label.contains(keyword, ignoreCase = true)
        }
    }
    
    /**
     * 释放资源
     */
    fun close() {
        labeler.close()
    }
}

/**
 * 植物识别结果
 */
data class PlantRecognitionResult(
    val success: Boolean,
    val plantName: String?,
    val confidence: Float,
    val allLabels: List<Label>,
    val errorMessage: String? = null
)

/**
 * 标签数据类
 */
data class Label(
    val text: String,
    val confidence: Float
)
