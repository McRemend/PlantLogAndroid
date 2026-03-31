package com.plantlog.app.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.plantlog.app.presentation.add.AddPlantScreen
import com.plantlog.app.presentation.detail.DetailScreen
import com.plantlog.app.presentation.home.HomeScreen

/**
 * 导航图定义
 */
sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Detail : Screen("detail/{plantId}") {
        fun createRoute(plantId: Long) = "detail/$plantId"
    }
    object Add : Screen("add")
}

/**
 * 应用导航主机
 */
@Composable
fun PlantLogNavHost(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        // 首页
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToDetail = { plantId ->
                    navController.navigate(Screen.Detail.createRoute(plantId))
                },
                onNavigateToAdd = {
                    navController.navigate(Screen.Add.route)
                }
            )
        }
        
        // 详情页
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
                onNavigateBack = {
                    navController.popBackStack()
                },
                onPlantUpdated = {
                    navController.popBackStack()
                }
            )
        }
        
        // 添加植物页
        composable(Screen.Add.route) {
            AddPlantScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onPlantAdded = {
                    navController.popBackStack()
                }
            )
        }
    }
}
