package com.uecesar.qrscanner.domain.model

import android.graphics.Bitmap
import com.uecesar.qrscanner.presentation.ui.enumerable.QrCodeType

data class QrCode(
    val id: String? = null,
    val content: String,
    val type: QrCodeType,
    val timestamp: Long = System.currentTimeMillis(),
    val isGenerated: Boolean = false,
    val userId: String? = null,
    val bitmap: Bitmap? = null
)

val qrCodesResponse: List<QrCode> = listOf(
    QrCode(id = "1", content = "https://www.google.com", type = QrCodeType.URL),
    QrCode(id = "2", content = "Hello, World!", type = QrCodeType.TEXT),
)