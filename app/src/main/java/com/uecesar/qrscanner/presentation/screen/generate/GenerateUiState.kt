package com.uecesar.qrscanner.presentation.screen.generate

import android.graphics.Bitmap
import com.uecesar.qrscanner.domain.model.QrCode

sealed class GenerateUiState {
    object InitialState : GenerateUiState()
    object Loading : GenerateUiState()
    data class Success(val qrCode: QrCode) : GenerateUiState()
    data class Error(val message: String) : GenerateUiState()
}