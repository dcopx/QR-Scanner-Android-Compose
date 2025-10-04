package com.uecesar.qrscanner.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import com.uecesar.qrscanner.presentation.navigation.LocalNavController
import com.uecesar.qrscanner.presentation.navigation.Routes
import com.uecesar.qrscanner.presentation.ui.theme.QRScannerTheme

@Composable
fun CustomBottomBar() {
    val navController = LocalNavController.current
    val currentDestination = navController
        .currentBackStackEntryAsState().value?.destination?.route

    BottomAppBar {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            AppBarItem(
                icon = Icons.Default.History,
                label = "History",
                selected = currentDestination == Routes.HISTORY
            ) { navController.navigate(Routes.HISTORY) { launchSingleTop = true } }
            AppBarItem(
                icon = Icons.Default.QrCodeScanner,
                label = "Scan",
                selected = currentDestination == Routes.SCANNER
            ) { navController.navigate(Routes.SCANNER) { launchSingleTop = true } }
            AppBarItem(
                icon = Icons.Default.QrCode,
                label = "Generate",
                selected = currentDestination == Routes.GENERATE
            ) { navController.navigate(Routes.GENERATE) { launchSingleTop = true } }
        }
    }
}

@Composable
private fun AppBarItem(
    icon: ImageVector,
    label: String,
    selected: Boolean,
    onClick: () -> Unit
){
    Box(
        modifier = Modifier
            .height(45.dp)
            .width(50.dp)
            .clip(MaterialTheme.shapes.small)
            .background(color = if (selected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceContainer)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(icon, contentDescription = null)
            Text(label, style = MaterialTheme.typography.labelSmall)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Previa(){
    QRScannerTheme(darkTheme = true, dynamicColor = false) {
        AppBarItem(
            icon = Icons.Default.QrCode,
            label = "Generate",
            selected = true
        ) {}
    }
}