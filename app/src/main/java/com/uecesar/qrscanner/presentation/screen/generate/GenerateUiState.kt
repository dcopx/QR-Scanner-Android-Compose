package com.uecesar.qrscanner.presentation.screen.generate

import com.uecesar.qrscanner.domain.model.QrCode

sealed class GenerateUiState {
    object Idle : GenerateUiState()
    object Loading : GenerateUiState()
    data class Success(val qrCode: QrCode) : GenerateUiState()
    data class Error(val message: String) : GenerateUiState()
}