package com.plantlog.app.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.plantlog.app.presentation.add.AddPlantScreen
import com.plantlog.app.presentation.detail.DetailScreen
import com.plantlog.app.presentation.home.HomeScreen

/**
 * 导航图构建扩展函数
 * 将导航逻辑分离到单独的文件以保持清晰
 */

fun NavGraphBuilder.homeScreen(
    onNavigateToDetail: (Long) -> Unit,
    onNavigateToAdd: () -> Unit
) {
    composable(Screen.Home.route) {
        HomeScreen(
            onNavigateToDetail = onNavigateToDetail,
            onNavigateToAdd = onNavigateToAdd
        )
    }
}

fun NavGraphBuilder.detailScreen(
    onNavigateBack: () -> Unit,
    onPlantUpdated: () -> Unit
) {
    composable(
        route = Screen.Detail.route,
        arguments = listOf(
            navArgument("plantId") {
                type = NavType.LongType
            }
        )
    ) { backStackEntry ->
        val plantId = backStackEntry.arguments?.getLong("plantId") ?: return@composable
        DetailScreen(
            plantId = plantId,
            onNavigateBack = onNavigateBack,
            onPlantUpdated = onPlantUpdated
        )
    }
}

fun NavGraphBuilder.addPlantScreen(
    onNavigateBack: () -> Unit,
    onPlantAdded: () -> Unit
) {
    composable(Screen.Add.route) {
        AddPlantScreen(
            onNavigateBack = onNavigateBack,
            onPlantAdded = onPlantAdded
        )
    }
}
