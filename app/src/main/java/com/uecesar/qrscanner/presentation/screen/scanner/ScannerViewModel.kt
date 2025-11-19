package com.uecesar.qrscanner.presentation.screen.scanner

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uecesar.qrscanner.domain.useCase.ScanQrCodeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScannerViewModel @Inject constructor(
    private val scanQrCodeUseCase: ScanQrCodeUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<ScannerUiState>(ScannerUiState.InitialState)
    val uiState: StateFlow<ScannerUiState> = _uiState.asStateFlow()

    private val _reloadRequestCameraPermission = mutableIntStateOf(0)
    val reloadRequestCameraPermission: State<Int> = _reloadRequestCameraPermission

    private val _cameraPermission = mutableStateOf(false)
    val cameraPermission: State<Boolean> = _cameraPermission

    fun onReloadRequestCameraPermissionIncreased() {
        _reloadRequestCameraPermission.intValue = _reloadRequestCameraPermission.intValue +1
    }
    fun onCameraPermissionChanged(value: Boolean) {
        _cameraPermission.value = value
    }


    fun onQrCodeScanned(content: String) {
        viewModelScope.launch {
            _uiState.value = ScannerUiState.Loading

            scanQrCodeUseCase(content)
                .onSuccess { qrCode ->
                    _uiState.value = ScannerUiState.Success(qrCode)
                }
                .onFailure { error ->
                    _uiState.value = ScannerUiState.Error(
                        error.message ?: "Unknown error occurred"
                    )
                }
        }
    }

    fun resetState() {
        _uiState.value = ScannerUiState.InitialState
    }
}