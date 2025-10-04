package com.uecesar.qrscanner.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.uecesar.qrscanner.presentation.screen.generate.GenerateScreen
import com.uecesar.qrscanner.presentation.screen.history.HistoryScreen
import com.uecesar.qrscanner.presentation.screen.scanner.ScannerScreen

@Composable
fun QrScannerApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.GENERATE
    ) {
        composable(Routes.SCANNER) {
            ScannerScreen(
                onNavigateToHistory = {
                    navController.navigate(Routes.HISTORY){ launchSingleTop = true }
                },
                onNavigateToGenerate = {
                    navController.navigate(Routes.GENERATE){ launchSingleTop = true }
                }
            )
        }

        composable(Routes.HISTORY) {
            HistoryScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Routes.GENERATE) {
            GenerateScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}