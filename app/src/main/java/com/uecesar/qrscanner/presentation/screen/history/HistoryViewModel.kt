package com.uecesar.qrscanner.presentation.screen.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uecesar.qrscanner.domain.model.QrCode
import com.uecesar.qrscanner.domain.repository.QrCodeRepository
import com.uecesar.qrscanner.domain.useCase.DeleteQrCodeUseCase
import com.uecesar.qrscanner.domain.useCase.GetQrCodesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val getQrCodesUseCase: GetQrCodesUseCase,
    private val deleteQrCodeUseCase: DeleteQrCodeUseCase,
    private val repository: QrCodeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<HistoryUiState>(HistoryUiState.Loading)
    val uiState: StateFlow<HistoryUiState> = _uiState.asStateFlow()

    private val _qrCodes = MutableStateFlow<List<QrCode>>(emptyList())
    val qrCodes: StateFlow<List<QrCode>> = _qrCodes.asStateFlow()

    fun loadQrCodes(page: Int = 0) {
        viewModelScope.launch {
            _uiState.value = HistoryUiState.Loading

            getQrCodesUseCase(page)
                .onSuccess { response ->
                    _qrCodes.value = response.data
                    _uiState.value = HistoryUiState.Success
                }
                .onFailure { error ->
                    _uiState.value = HistoryUiState.Error(
                        error.message ?: "Failed to load QR codes"
                    )
                }
        }
    }

    fun deleteQrCode(id: String) {
        viewModelScope.launch {
            deleteQrCodeUseCase(id)
                .onSuccess {
                    // Refresh list
                    loadQrCodes()
                }
                .onFailure { error ->
                    _uiState.value = HistoryUiState.Error(
                        error.message ?: "Failed to delete QR code"
                    )
                }
        }
    }

    fun deleteAllQrCodes() {
        viewModelScope.launch {
            repository.deleteAllQrCodes()
                .onSuccess {
                    _qrCodes.value = emptyList()
                    _uiState.value = HistoryUiState.Success
                }
                .onFailure { error ->
                    _uiState.value = HistoryUiState.Error(
                        error.message ?: "Failed to delete all QR codes"
                    )
                }
        }
    }
}