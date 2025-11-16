package com.uecesar.qrscanner.presentation.components.dialog

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.uecesar.qrscanner.presentation.ui.util.SettingsUtils

@Composable
fun CustomGoToSettingsDialog(
    context: Context,
    title: String,
    message: String,
    onDismissRequest: () -> Unit
){
    CustomBasicDialog(
        onDismissRequest = onDismissRequest
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(Modifier.height(20.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(20.dp))
            Botones(
                onOpenSettings = {
                    SettingsUtils.openAppSettings(context)
                    onDismissRequest()
                },
                onDismiss = onDismissRequest
            )
        }
    }
}

@Composable
private fun Botones(
    onOpenSettings: () -> Unit,
    onDismiss: () -> Unit
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        ElevatedButton ( onClick = onDismiss ) {
            Text(
                text = "Cancelar",
                style = MaterialTheme.typography.labelSmall
            )
        }

        ElevatedButton (
            onClick = onOpenSettings,
            colors = ButtonDefaults.elevatedButtonColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Text(
                text = "Ir a Configuración",
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}