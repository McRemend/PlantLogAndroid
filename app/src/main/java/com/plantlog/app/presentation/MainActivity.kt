package com.plantlog.app.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.plantlog.app.presentation.navigation.PlantLogNavHost
import com.plantlog.app.ui.theme.PlantLogTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * 主 Activity
 * 应用入口点，设置 Compose 内容和导航
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        setContent {
            PlantLogTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    PlantLogNavHost()
                }
            }
        }
    }
}
