package com.uecesar.qrscanner.presentation.screen.scanner

import com.uecesar.qrscanner.domain.model.QrCode

sealed class ScannerUiState {
    object InitialState : ScannerUiState()
    object Loading : ScannerUiState()
    data class Success(val qrCode: QrCode) : ScannerUiState()
    data class Error(val message: String) : ScannerUiState()
}