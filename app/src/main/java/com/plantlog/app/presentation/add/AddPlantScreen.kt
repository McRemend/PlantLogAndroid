package com.plantlog.app.presentation.add

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage

/**
 * 添加植物页面
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPlantScreen(
    onNavigateBack: () -> Unit,
    onPlantAdded: () -> Unit,
    viewModel: AddPlantViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
    // 图片选择器
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            viewModel.setImageUri(it)
            viewModel.identifyPlant(it)
        }
    }
    
    // 处理保存成功
    if (uiState.saveSuccess) {
        LaunchedEffect(uiState.saveSuccess) {
            viewModel.clearSuccess()
            onPlantAdded()
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("添加植物") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "返回"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = viewModel::savePlant,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Text("保存")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 图片上传区域
                ImageUploadSection(
                    imageUri = uiState.imageUri,
                    isRecognizing = uiState.isRecognizing,
                    recognitionConfidence = uiState.recognitionConfidence,
                    showRecognitionSuccess = uiState.showRecognitionSuccess,
                    onPickImage = { imagePickerLauncher.launch("image/*") },
                    onClearImage = { viewModel.setImageUri(null) }
                )
                
                // 识别错误提示
                uiState.recognitionError?.let { error ->
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "⚠️ $error",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )
                            IconButton(onClick = { viewModel.clearError() }) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "关闭",
                                    tint = MaterialTheme.colorScheme.onErrorContainer
                                )
                            }
                        }
                    }
                }
                
                // 基本信息
                OutlinedTextField(
                    value = uiState.plantName,
                    onValueChange = viewModel::updatePlantName,
                    label = { Text("植物名称 *") },
                    placeholder = { Text("例如：绿萝") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                
                OutlinedTextField(
                    value = uiState.scientificName,
                    onValueChange = viewModel::updateScientificName,
                    label = { Text("学名") },
                    placeholder = { Text("例如：Epipremnum aureum") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                
                OutlinedTextField(
                    value = uiState.nickname,
                    onValueChange = viewModel::updateNickname,
                    label = { Text("昵称") },
                    placeholder = { Text("给你的植物起个可爱的名字") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                
                // 浇水间隔
                WaterIntervalSelector(
                    days = uiState.waterInterval,
                    onDaysChanged = viewModel::updateWaterInterval
                )
                
                // 备注
                OutlinedTextField(
                    value = uiState.notes,
                    onValueChange = viewModel::updateNotes,
                    label = { Text("备注") },
                    placeholder = { Text("记录植物的特殊需求或注意事项") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    maxLines = 4
                )
                
                Spacer(modifier = Modifier.height(80.dp))
            }
            
            // 错误提示
            uiState.error?.let { error ->
                Snackbar(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp),
                    action = {
                        TextButton(onClick = viewModel::clearError) {
                            Text("关闭")
                        }
                    }
                ) {
                    Text(error)
                }
            }
        }
    }
}

/**
 * 图片上传区域
 */
@Composable
private fun ImageUploadSection(
    imageUri: Uri?,
    isRecognizing: Boolean,
    recognitionConfidence: Float,
    showRecognitionSuccess: Boolean,
    onPickImage: () -> Unit,
    onClearImage: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            if (imageUri != null) {
                // 显示已选择的图片
                AsyncImage(
                    model = imageUri,
                    contentDescription = "植物图片",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                
                // 清除按钮
                IconButton(
                    onClick = onClearImage,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "清除图片",
                        tint = MaterialTheme.colorScheme.onError,
                        modifier = Modifier.size(24.dp)
                    )
                }
                
                // 识别状态
                if (isRecognizing) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .align(Alignment.Center),
                        contentAlignment = Alignment.Center
                    ) {
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
                            )
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    strokeWidth = 2.dp
                                )
                                Text("正在识别...")
                            }
                        }
                    }
                } else if (showRecognitionSuccess) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter)
                            .padding(8.dp)
                    ) {
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("✅")
                                Text(
                                    text = "识别成功 (${(recognitionConfidence * 100).toInt()}%)",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                }
            } else {
                // 空状态 - 选择图片按钮
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    IconButton(
                        onClick = onPickImage,
                        modifier = Modifier.size(64.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = "选择图片",
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "点击拍摄或选择植物照片",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "AI 将自动识别植物品种",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

/**
 * 浇水间隔选择器
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun WaterIntervalSelector(
    days: Int,
    onDaysChanged: (Int) -> Unit
) {
    Column {
        Text(
            text = "浇水间隔",
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(8.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf(3, 5, 7, 10, 14, 21).forEach { interval ->
                FilterChip(
                    selected = days == interval,
                    onClick = { onDaysChanged(interval) },
                    label = { Text("$interval 天") },
                    modifier = Modifier.weight(1f)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("自定义:")
            OutlinedTextField(
                value = days.toString(),
                onValueChange = { 
                    it.toIntOrNull()?.let { d -> onDaysChanged(d) }
                },
                modifier = Modifier.width(80.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )
            Text("天")
        }
    }
}
