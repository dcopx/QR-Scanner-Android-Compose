package com.uecesar.qrscanner.presentation.screen.scanner

import android.Manifest
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.uecesar.qrscanner.presentation.components.CameraPreview
import com.uecesar.qrscanner.presentation.components.CustomAppBar
import com.uecesar.qrscanner.presentation.components.PermissionDeniedContent
import com.uecesar.qrscanner.presentation.components.ScanningOverlay

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ScannerScreen(
    onNavigateToHistory: () -> Unit,
    onNavigateToGenerate: () -> Unit,
    viewModel: ScannerViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)

    LaunchedEffect(Unit) {
        if (!cameraPermissionState.status.isGranted) {
            cameraPermissionState.launchPermissionRequest()
        }
    }

    Scaffold(
        topBar = {
            CustomAppBar(
                title = "QR Scanner",
                actions =  {
                    IconButton(onClick = onNavigateToHistory) {
                        Icon(Icons.Default.History, contentDescription = "History")
                    }
                    IconButton(onClick = onNavigateToGenerate) {
                        Icon(Icons.Default.QrCode, contentDescription = "Generate")
                    }
                }
            )
        },
        bottomBar = { CustomBottomAppBar(onNavigateToHistory, onNavigateToGenerate) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (cameraPermissionState.status.isGranted) {
                CameraPreview { content -> viewModel.onQrCodeScanned(content) }
                ScanningOverlay()

                // Status message
                when (val state = uiState) {
                    is ScannerUiState.Loading -> { LoadingState(Modifier.align(Alignment.BottomCenter)) }
                    is ScannerUiState.Success -> { SuccessState(Modifier.align(Alignment.BottomCenter), state) }
                    is ScannerUiState.Error -> { ErrorState(Modifier.align(Alignment.BottomCenter), state) }
                    else -> {}
                }

            } else {
                PermissionDeniedContent {
                    cameraPermissionState.launchPermissionRequest()
                }
            }
        }
    }
}

@Composable
private fun CustomBottomAppBar(
    onNavigateToHistory: () -> Unit,
    onNavigateToGenerate: () -> Unit
) {
    BottomAppBar {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            AppBarItem(Icons.Default.History, "History") {onNavigateToHistory}
            AppBarItem(Icons.Default.QrCodeScanner, "Scan") { /* Handle scan action */ }
            AppBarItem(Icons.Default.QrCode, "Generate") {onNavigateToGenerate }
        }
    }
}

@Composable
private fun CustomCard(
    modifier: Modifier = Modifier,
    containerColor: Color,
    content: @Composable () -> Unit
){
    Card(
        modifier = modifier.padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor)
    ) { content() }
}

@Composable
private fun LoadingState( modifier: Modifier = Modifier ){
    CustomCard(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.primaryContainer
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                strokeWidth = 2.dp
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                "Processing QR code...",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun SuccessState(
    modifier: Modifier = Modifier,
    state: ScannerUiState.Success
){
    CustomCard(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.secondaryContainer
    ){
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "QR Code Scanned!",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Type: ${state.qrCode.type.name}",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                state.qrCode.content.take(50) + if (state.qrCode.content.length > 50) "..." else "",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
private fun ErrorState(
    modifier: Modifier = Modifier,
    state: ScannerUiState.Error
){
    CustomCard(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.errorContainer
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Error,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                state.message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onErrorContainer
            )
        }
    }
}

@Composable
private fun AppBarItem(icon: ImageVector, label: String, onClick: () -> Unit){
    IconButton(onClick = onClick) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(icon, contentDescription = "History")
            Text(label, style = MaterialTheme.typography.labelSmall)
        }
    }
}