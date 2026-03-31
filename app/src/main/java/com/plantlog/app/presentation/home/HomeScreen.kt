package com.plantlog.app.presentation.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.plantlog.app.domain.model.Plant
import java.text.SimpleDateFormat
import java.util.*

/**
 * 首页 - 植物列表
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToDetail: (Long) -> Unit,
    onNavigateToAdd: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
    // 处理错误
    uiState.error?.let { error ->
        LaunchedEffect(error) {
            // 可以显示 Snackbar
            viewModel.clearError()
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("植记 PlantLog") },
                actions = {
                    // 提醒通知
                    if (uiState.plantsNeedWaterCount > 0) {
                        IconButton(onClick = { /* TODO: 显示需要浇水的植物 */ }) {
                            Badge {
                                Text(uiState.plantsNeedWaterCount.toString())
                            }
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "浇水提醒"
                            )
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToAdd,
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "添加植物"
                )
            }
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
                uiState.plants.isEmpty() -> {
                    EmptyState(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                else -> {
                    PlantList(
                        plants = uiState.plants,
                        onPlantClick = onNavigateToDetail,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

/**
 * 植物列表
 */
@Composable
private fun PlantList(
    plants: List<Plant>,
    onPlantClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(plants, key = { it.id }) { plant ->
            PlantCard(
                plant = plant,
                onClick = { onPlantClick(plant.id) }
            )
        }
    }
}

/**
 * 植物卡片
 */
@Composable
private fun PlantCard(
    plant: Plant,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 植物图片
            AsyncImage(
                model = plant.imageUrl,
                contentDescription = plant.name,
                modifier = Modifier
                    .size(64.dp)
                    .padding(end = 12.dp),
                contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                alignment = androidx.compose.ui.Alignment.Center
            )
            
            // 植物信息
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = plant.nickname ?: plant.name,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                
                if (plant.scientificName != null) {
                    Text(
                        text = plant.scientificName,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                Spacer(modifier = Modifier.height(4.dp))
                
                // 浇水状态
                val daysLeft = plant.daysUntilNextWater()
                Text(
                    text = if (plant.needsWater()) {
                        "⚠️ 需要浇水"
                    } else {
                        "💧 $daysLeft 天后浇水"
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = if (plant.needsWater()) {
                        MaterialTheme.colorScheme.error
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    }
                )
            }
            
            // 收藏标记
            if (plant.isFavorite) {
                Icon(
                    imageVector = Icons.Default.Notifications, // TODO: 使用心形图标
                    contentDescription = "收藏",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

/**
 * 空状态
 */
@Composable
private fun EmptyState(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "🌱",
            style = MaterialTheme.typography.displayLarge
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "还没有植物",
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "点击右下角按钮添加你的第一盆植物",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
