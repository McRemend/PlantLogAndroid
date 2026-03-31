package com.plantlog.app.presentation.detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.plantlog.app.data.local.CareType
import java.text.SimpleDateFormat
import java.util.*

/**
 * 植物详情页
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    plantId: Long,
    onNavigateBack: () -> Unit,
    onPlantUpdated: () -> Unit,
    viewModel: DetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
    // 加载植物数据
    LaunchedEffect(plantId) {
        viewModel.loadPlant(plantId)
    }
    
    // 处理成功提示
    if (uiState.showWaterSuccess) {
        LaunchedEffect(uiState.showWaterSuccess) {
            viewModel.clearSuccess()
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("植物详情") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "返回"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO: 编辑植物 */ }) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "编辑"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                uiState.plant == null -> {
                    Text(
                        text = "植物不存在",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                else -> {
                    PlantDetailContent(
                        plant = uiState.plant!!,
                        daysUntilWater = uiState.daysUntilWater,
                        onWaterClick = { viewModel.waterPlant(plantId) },
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

/**
 * 植物详情内容
 */
@Composable
private fun PlantDetailContent(
    plant: com.plantlog.app.domain.model.Plant,
    daysUntilWater: Int,
    onWaterClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // 植物图片
        AsyncImage(
            model = plant.imageUrl,
            contentDescription = plant.name,
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp),
            contentScale = ContentScale.Crop
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // 植物名称
        Text(
            text = plant.nickname ?: plant.name,
            style = MaterialTheme.typography.headlineLarge
        )
        
        if (plant.scientificName != null) {
            Text(
                text = plant.scientificName,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // 浇水状态卡片
        WaterStatusCard(
            plant = plant,
            daysUntilWater = daysUntilWater,
            onWaterClick = onWaterClick
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // 植物信息
        PlantInfoSection(plant = plant)
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // 护理记录
        CareRecordsSection(plantId = plant.id)
        
        Spacer(modifier = Modifier.height(32.dp))
    }
}

/**
 * 浇水状态卡片
 */
@Composable
private fun WaterStatusCard(
    plant: com.plantlog.app.domain.model.Plant,
    daysUntilWater: Int,
    onWaterClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (plant.needsWater()) {
                MaterialTheme.colorScheme.errorContainer
            } else {
                MaterialTheme.colorScheme.primaryContainer
            }
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = if (plant.needsWater()) {
                    "⚠️ 需要浇水"
                } else {
                    "💧 浇水提醒"
                },
                style = MaterialTheme.typography.titleMedium
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = if (plant.needsWater()) {
                    "这盆植物已经 overdue 了，请尽快浇水！"
                } else {
                    "预计 $daysUntilWater 天后需要浇水"
                },
                style = MaterialTheme.typography.bodyMedium
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Button(
                onClick = onWaterClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("记录浇水")
            }
        }
    }
}

/**
 * 植物信息区域
 */
@Composable
private fun PlantInfoSection(
    plant: com.plantlog.app.domain.model.Plant
) {
    Text(
        text = "植物信息",
        style = MaterialTheme.typography.titleMedium
    )
    
    Spacer(modifier = Modifier.height(8.dp))
    
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        InfoRow("浇水间隔", "${plant.waterInterval} 天")
        InfoRow(
            "上次浇水",
            formatDate(plant.lastWatered)
        )
        InfoRow(
            "获取日期",
            formatDate(plant.acquiredDate)
        )
        if (plant.notes != null) {
            InfoRow("备注", plant.notes)
        }
    }
}

/**
 * 信息行
 */
@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

/**
 * 护理记录区域
 */
@Composable
private fun CareRecordsSection(
    plantId: Long
) {
    Text(
        text = "护理记录",
        style = MaterialTheme.typography.titleMedium
    )
    
    Spacer(modifier = Modifier.height(8.dp))
    
    // TODO: 显示护理记录列表
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "暂无护理记录",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AssistChip(
                    onClick = { /* TODO: 添加浇水记录 */ },
                    label = { Text("💧 浇水") }
                )
                AssistChip(
                    onClick = { /* TODO: 添加施肥记录 */ },
                    label = { Text("🌿 施肥") }
                )
                AssistChip(
                    onClick = { /* TODO: 添加修剪记录 */ },
                    label = { Text("✂️ 修剪") }
                )
            }
        }
    }
}

/**
 * 格式化日期
 */
private fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return sdf.format(Date(timestamp))
}
