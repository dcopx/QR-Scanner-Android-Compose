package com.uecesar.qrscanner.presentation.screen.scanner

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.uecesar.qrscanner.presentation.components.CameraView
import com.uecesar.qrscanner.presentation.components.CustomAppBar
import com.uecesar.qrscanner.presentation.components.CustomBottomBar
import com.uecesar.qrscanner.presentation.components.CustomCard
import com.uecesar.qrscanner.presentation.components.CameraPermissionDeniedContent
import com.uecesar.qrscanner.presentation.components.RequestCameraPermission
import com.uecesar.qrscanner.presentation.components.ScanningOverlay

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ScannerScreen(
    viewModel: ScannerViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val cameraPermission by viewModel.cameraPermission
    val reloadRequest by viewModel.reloadRequestCameraPermission

    Scaffold(
        topBar = {
            CustomAppBar(
                title = "QR Scanner"
            )
        },
        bottomBar = { CustomBottomBar() }
    ) { paddingValues ->
        ValidateCameraPermission(reloadRequest){
            viewModel.onCameraPermissionChanged(it)
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (cameraPermission) {
                CameraView { content -> viewModel.onQrCodeScanned(content) }
                ScanningOverlay()

                when (val state = uiState) {
                    is ScannerUiState.Loading -> { LoadingState(Modifier.align(Alignment.BottomCenter)) }
                    is ScannerUiState.Success -> { SuccessState(Modifier.align(Alignment.BottomCenter), state) }
                    is ScannerUiState.Error -> { ErrorState(Modifier.align(Alignment.BottomCenter), state) }
                    else -> {}
                }

            } else {
                CameraPermissionDeniedContent {
                    viewModel.onReloadRequestCameraPermissionIncreased()
                }
            }
        }
    }
}

@Composable
private fun ValidateCameraPermission(reloadRequest: Int, onPermissionResult: (Boolean) -> Unit){
    key(reloadRequest) {
        RequestCameraPermission { granted ->
            onPermissionResult(granted)
        }
    }
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
