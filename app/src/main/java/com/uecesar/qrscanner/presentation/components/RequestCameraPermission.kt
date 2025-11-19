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

/**
 * Composable que maneja la solicitud de permiso de la cámara, consulta cada vez que se dibuja el componente.
 * Si ya se ha denegado el permiso mostrará un dialog para ir a la configuración.
 * @param onPermissionResult Callback que se llama cuando se obtiene el resultado del permiso.
 */
@Composable
fun RequestCameraPermission(
    onPermissionResult: (Boolean) -> Unit
) {
    val context = LocalContext.current
    val activity = context as Activity
    val permission = Manifest.permission.CAMERA

    var showSettingsDialog by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            onPermissionResult(true)
        } else {
            val permanentlyDenied =
                !ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)

            if (permanentlyDenied) {
                showSettingsDialog = true
            }

            onPermissionResult(false)
        }
    }

    LaunchedEffect(Unit) {
        val granted = ContextCompat.checkSelfPermission(
            context, permission
        ) == PackageManager.PERMISSION_GRANTED

        if (!granted) {
            launcher.launch(permission) //Pedir permiso
        } else {
            onPermissionResult(true)
        }
    }

    if (showSettingsDialog) {
        CustomGoToSettingsDialog(
            context = context,
            title = "Permiso requerido",
            message = "Necesitamos acceso a la cámara para leer QR."
        ) {
            showSettingsDialog = false
        }
    }
}