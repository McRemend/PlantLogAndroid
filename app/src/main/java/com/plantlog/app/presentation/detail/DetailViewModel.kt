package com.plantlog.app.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plantlog.app.data.local.CareRecordEntity
import com.plantlog.app.data.local.CareType
import com.plantlog.app.domain.model.CareRecord
import com.plantlog.app.domain.model.Plant
import com.plantlog.app.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 详情页 ViewModel
 */
@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getPlantByIdUseCase: GetPlantByIdUseCase,
    private val waterPlantUseCase: WaterPlantUseCase,
    private val addPlantUseCase: AddPlantUseCase,
    private val getAllPlantsUseCase: GetAllPlantsUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()
    
    /**
     * 加载植物详情
     */
    fun loadPlant(plantId: Long) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            val plant = getPlantByIdUseCase(plantId)
            if (plant != null) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    plant = plant,
                    daysUntilWater = plant.daysUntilNextWater()
                )
            } else {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "植物不存在"
                )
            }
        }
    }
    
    /**
     * 浇水操作
     */
    fun waterPlant(plantId: Long) {
        viewModelScope.launch {
            try {
                waterPlantUseCase(plantId)
                // 重新加载植物信息
                loadPlant(plantId)
                _uiState.value = _uiState.value.copy(
                    showWaterSuccess = true
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message
                )
            }
        }
    }
    
    /**
     * 添加护理记录
     */
    fun addCareRecord(plantId: Long, careType: CareType, notes: String? = null) {
        viewModelScope.launch {
            try {
                val record = CareRecordEntity(
                    plantId = plantId,
                    careType = careType,
                    notes = notes
                )
                // TODO: 需要通过 repository 添加
                _uiState.value = _uiState.value.copy(
                    showCareSuccess = true
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message
                )
            }
        }
    }
    
    /**
     * 清除成功提示
     */
    fun clearSuccess() {
        _uiState.value = _uiState.value.copy(
            showWaterSuccess = false,
            showCareSuccess = false
        )
    }
    
    /**
     * 清除错误
     */
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}

/**
 * 详情页 UI 状态
 */
data class DetailUiState(
    val isLoading: Boolean = true,
    val plant: Plant? = null,
    val daysUntilWater: Int = 0,
    val careRecords: List<CareRecord> = emptyList(),
    val showWaterSuccess: Boolean = false,
    val showCareSuccess: Boolean = false,
    val error: String? = null
)
