package com.uecesar.qrscanner.presentation.screen.generate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uecesar.qrscanner.domain.model.QrCodeContent
import com.uecesar.qrscanner.domain.useCase.GenerateQrCodeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GenerateViewModel @Inject constructor(
    private val generateQrCodeUseCase: GenerateQrCodeUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<GenerateUiState>(GenerateUiState.Idle)
    val uiState: StateFlow<GenerateUiState> = _uiState.asStateFlow()

    fun generateQrCode(content: QrCodeContent) {
        viewModelScope.launch {
            _uiState.value = GenerateUiState.Loading

            generateQrCodeUseCase(content)
                .onSuccess { qrCode ->
                    _uiState.value = GenerateUiState.Success(qrCode)
                }
                .onFailure { error ->
                    _uiState.value = GenerateUiState.Error(
                        error.message ?: "Failed to generate QR code"
                    )
                }
        }
    }

    fun resetState() {
        _uiState.value = GenerateUiState.Idle
    }
}