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
        startDestination = "scanner"
    ) {
        composable("scanner") {
            ScannerScreen(
                onNavigateToHistory = {
                    navController.navigate("history")
                },
                onNavigateToGenerate = {
                    navController.navigate("generate")
                }
            )
        }

        composable("history") {
            HistoryScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable("generate") {
            GenerateScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}