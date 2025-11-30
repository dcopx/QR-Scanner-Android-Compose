package com.uecesar.qrscanner.viewmodel

import app.cash.turbine.test
import com.uecesar.qrscanner.domain.model.QrCode
import com.uecesar.qrscanner.domain.useCase.ScanQrCodeUseCase
import com.uecesar.qrscanner.presentation.screen.scanner.ScannerUiState
import com.uecesar.qrscanner.presentation.screen.scanner.ScannerViewModel
import com.uecesar.qrscanner.presentation.ui.enumerable.QrCodeType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class ScannerViewModelTest {

    private val scanUseCase = mock<ScanQrCodeUseCase>()
    private lateinit var viewModel: ScannerViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
        viewModel = ScannerViewModel(scanUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `cuando el scan es exitoso cambia a Success`() = runTest {
        val qr = QrCode(
            id = "10",
            content = "aea",
            type = QrCodeType.TEXT
        )

        whenever(scanUseCase("123")).thenReturn(Result.success(qr))

        viewModel.onQrCodeScanned("123")

        viewModel.uiState.test {
            assert(awaitItem() is ScannerUiState.Loading)
            assert(awaitItem() == ScannerUiState.Success(qr))
        }
    }

    @Test
    fun `cuando el scan falla cambia a Error`() = runTest {
        whenever(scanUseCase("ERR")).thenReturn(Result.failure(Exception("Falló")))

        viewModel.onQrCodeScanned("ERR")

        viewModel.uiState.test {
            assert(awaitItem() is ScannerUiState.Loading)
            val error = awaitItem()
            assert(error is ScannerUiState.Error)
            assert((error as ScannerUiState.Error).message == "Falló")
        }
    }
}
