package com.uecesar.qrscanner.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ScanningOverlay(
    modifier: Modifier = Modifier,
    borderColor: Color = Color.Green,
    borderStroke: Dp = 4.dp,
    cornerRadius: Dp = 16.dp
) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height

        // definimos un rectángulo central
        val rectWidth = width * 0.7f
        val rectHeight = height * 0.4f
        val left = (width - rectWidth) / 2f
        val top = (height - rectHeight) / 2f

        // dibujamos un overlay semitransparente
        drawRect(
            color = Color.Black.copy(alpha = 0.5f),
            size = size
        )

        // “recortamos” la zona central para mostrar la cámara
        drawRoundRect(
            color = Color.Transparent,
            topLeft = Offset(left, top),
            size = Size(rectWidth, rectHeight),
            cornerRadius = CornerRadius(cornerRadius.toPx(), cornerRadius.toPx()),
            blendMode = BlendMode.Clear
        )

        // dibujamos el borde verde del área de escaneo
        drawRoundRect(
            color = borderColor,
            topLeft = Offset(left, top),
            size = Size(rectWidth, rectHeight),
            style = Stroke(width = borderStroke.toPx()),
            cornerRadius = CornerRadius(cornerRadius.toPx(), cornerRadius.toPx())
        )
    }
}
