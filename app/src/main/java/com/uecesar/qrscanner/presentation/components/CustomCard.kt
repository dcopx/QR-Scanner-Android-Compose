package com.uecesar.qrscanner.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.uecesar.qrscanner.presentation.ui.theme.QRScannerTheme

@Composable
fun CustomCard(
    modifier: Modifier = Modifier,
    containerColor: Color,
    content: @Composable () -> Unit
){
    Card(
        modifier = modifier.padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = containerColor
        )
    ) {
        Box(modifier = Modifier.padding(5.dp)) {
            content()
        }
    }
}

@Preview(name = "Dentro del Theme", showBackground = true)
@Composable
fun CustomCardPreviewInTheme() {
    QRScannerTheme(dynamicColor = false){
        CustomCard(containerColor = MaterialTheme.colorScheme.primaryContainer) {
            Text("Previa dentro del theme")
        }
    }
}
