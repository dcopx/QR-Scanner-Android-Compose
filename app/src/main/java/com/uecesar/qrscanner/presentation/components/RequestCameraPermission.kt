package com.uecesar.qrscanner.presentation.components

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.uecesar.qrscanner.presentation.components.dialog.CustomGoToSettingsDialog

@Composable
fun RequestCameraPermission(
    onRequestPermission: (granted: Boolean) -> Unit
) {
    val context = LocalContext.current
    val activity = context as Activity

    val permission = Manifest.permission.CAMERA

    // Para mostrar el diálogo "Ir a Configuración"
    var showSettingsDialog by remember { mutableStateOf(false) }

    // Launcher para pedir permiso al sistema
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            onRequestPermission(true)
        } else {
            // Detectar negación permanente (Android 13+)
            val permanentlyDenied =
                !ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)

            if (permanentlyDenied) {
                showSettingsDialog = true
            }

            onRequestPermission(false)
        }
    }

    // Lanzar el permiso la primera vez
    LaunchedEffect(Unit) {
        val granted = ContextCompat.checkSelfPermission( context, permission ) == PackageManager.PERMISSION_GRANTED
        if (granted) {
            onRequestPermission(true)
        } else {
            launcher.launch(permission)
        }
    }

    // Dialogo para ir a Configuración
    if (showSettingsDialog) {
        CustomGoToSettingsDialog(
            context = context,
            title = "Permiso para usar la cámara",
            message = "Para poder leer códigos QR, la App necesita el permiso para usar la cámara."
        ) {
            showSettingsDialog = false
        }
    }
}
