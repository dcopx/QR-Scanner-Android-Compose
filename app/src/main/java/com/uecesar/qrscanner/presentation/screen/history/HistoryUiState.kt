package com.uecesar.qrscanner.presentation.screen.history

sealed class HistoryUiState {
    object Loading : HistoryUiState()
    object Success : HistoryUiState()
    data class Error(val message: String) : HistoryUiState()
}