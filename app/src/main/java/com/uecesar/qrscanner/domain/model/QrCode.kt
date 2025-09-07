package com.uecesar.qrscanner.domain.model

import com.uecesar.qrscanner.presentation.ui.enumerable.QrCodeType

data class QrCode(
    val id: String? = null,
    val content: String,
    val type: QrCodeType,
    val timestamp: Long = System.currentTimeMillis(),
    val isGenerated: Boolean = false,
    val userId: String? = null
)