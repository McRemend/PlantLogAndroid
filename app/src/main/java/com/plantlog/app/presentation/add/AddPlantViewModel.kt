package com.plantlog.app.presentation.add

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plantlog.app.domain.model.Plant
import com.plantlog.app.domain.usecase.AddPlantUseCase
import com.plantlog.app.service.PlantRecognitionResult
import com.plantlog.app.service.PlantRecognitionService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 添加植物页 ViewModel
 */
@HiltViewModel
class AddPlantViewModel @Inject constructor(
    private val addPlantUseCase: AddPlantUseCase,
    private val plantRecognitionService: PlantRecognitionService
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(AddPlantUiState())
    val uiState: StateFlow<AddPlantUiState> = _uiState.asStateFlow()
    
    /**
     * 更新植物名称
     */
    fun updatePlantName(name: String) {
        _uiState.value = _uiState.value.copy(plantName = name)
    }
    
    /**
     * 更新学名
     */
    fun updateScientificName(name: String) {
        _uiState.value = _uiState.value.copy(scientificName = name)
    }
    
    /**
     * 更新昵称
     */
    fun updateNickname(nickname: String) {
        _uiState.value = _uiState.value.copy(nickname = nickname)
    }
    
    /**
     * 更新浇水间隔
     */
    fun updateWaterInterval(days: Int) {
        _uiState.value = _uiState.value.copy(waterInterval = days.coerceIn(1, 30))
    }
    
    /**
     * 更新备注
     */
    fun updateNotes(notes: String) {
        _uiState.value = _uiState.value.copy(notes = notes)
    }
    
    /**
     * 设置图片 URI
     */
    fun setImageUri(uri: Uri?) {
        _uiState.value = _uiState.value.copy(imageUri = uri)
    }
    
    /**
     * 识别图片中的植物
     */
    fun identifyPlant(imageUri: Uri) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isRecognizing = true)
            
            try {
                val result = plantRecognitionService.identifyPlantFromUri(imageUri)
                
                if (result.success) {
                    _uiState.value = _uiState.value.copy(
                        isRecognizing = false,
                        plantName = result.plantName ?: "",
                        recognitionConfidence = result.confidence,
                        showRecognitionSuccess = true
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        isRecognizing = false,
                        recognitionError = result.errorMessage ?: "识别失败"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isRecognizing = false,
                    recognitionError = e.message ?: "识别失败"
                )
            }
        }
    }
    
    /**
     * 保存植物
     */
    fun savePlant() {
        viewModelScope.launch {
            val state = _uiState.value
            
            // 验证必填字段
            if (state.plantName.isBlank()) {
                _uiState.value = state.copy(error = "请输入植物名称")
                return@launch
            }
            
            try {
                val plant = Plant(
                    name = state.plantName.trim(),
                    scientificName = state.scientificName.trim().takeIf { it.isNotBlank() },
                    nickname = state.nickname.trim().takeIf { it.isNotBlank() },
                    imageUrl = state.imageUri?.toString(),
                    waterInterval = state.waterInterval,
                    notes = state.notes.trim().takeIf { it.isNotBlank() }
                )
                
                val plantId = addPlantUseCase(plant)
                
                _uiState.value = state.copy(
                    saveSuccess = true,
                    savedPlantId = plantId
                )
            } catch (e: Exception) {
                _uiState.value = state.copy(
                    error = e.message ?: "保存失败"
                )
            }
        }
    }
    
    /**
     * 清除错误
     */
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
    
    /**
     * 清除成功提示
     */
    fun clearSuccess() {
        _uiState.value = _uiState.value.copy(
            saveSuccess = false,
            showRecognitionSuccess = false
        )
    }
}

/**
 * 添加植物页 UI 状态
 */
data class AddPlantUiState(
    val plantName: String = "",
    val scientificName: String = "",
    val nickname: String = "",
    val waterInterval: Int = 7,
    val notes: String = "",
    val imageUri: Uri? = null,
    val isRecognizing: Boolean = false,
    val recognitionConfidence: Float = 0f,
    val showRecognitionSuccess: Boolean = false,
    val recognitionError: String? = null,
    val saveSuccess: Boolean = false,
    val savedPlantId: Long? = null,
    val error: String? = null
)
