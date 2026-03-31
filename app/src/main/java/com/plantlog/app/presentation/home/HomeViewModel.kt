package com.plantlog.app.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plantlog.app.domain.model.Plant
import com.plantlog.app.domain.usecase.GetAllPlantsUseCase
import com.plantlog.app.domain.usecase.GetPlantsNeedWaterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 首页 ViewModel
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAllPlantsUseCase: GetAllPlantsUseCase,
    private val getPlantsNeedWaterUseCase: GetPlantsNeedWaterUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
    
    init {
        loadPlants()
    }
    
    /**
     * 加载植物列表
     */
    private fun loadPlants() {
        viewModelScope.launch {
            getAllPlantsUseCase()
                .catch { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = e.message
                    )
                }
                .collect { plants ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        plants = plants,
                        plantsNeedWaterCount = plants.count { it.needsWater() }
                    )
                }
        }
    }
    
    /**
     * 刷新数据
     */
    fun refresh() {
        _uiState.value = _uiState.value.copy(isLoading = true)
        loadPlants()
    }
    
    /**
     * 清除错误
     */
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}

/**
 * 首页 UI 状态
 */
data class HomeUiState(
    val isLoading: Boolean = true,
    val plants: List<Plant> = emptyList(),
    val plantsNeedWaterCount: Int = 0,
    val error: String? = null
)
