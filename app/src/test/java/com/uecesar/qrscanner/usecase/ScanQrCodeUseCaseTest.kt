package com.uecesar.qrscanner.usecase

import com.uecesar.qrscanner.domain.model.QrCode
import com.uecesar.qrscanner.domain.repository.QrCodeRepository
import com.uecesar.qrscanner.domain.useCase.ScanQrCodeUseCase
import com.uecesar.qrscanner.presentation.ui.enumerable.QrCodeType
import com.uecesar.qrscanner.security.QrContentParser
import com.uecesar.qrscanner.security.SecurityValidator
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

class ScanQrCodeUseCaseTest {

    private val repo = mock<QrCodeRepository>()
    private val parser = mock<QrContentParser>()
    private val validator = mock<SecurityValidator>()

    private val useCase = ScanQrCodeUseCase(repo, parser, validator)

    @Test
    fun `cuando el contenido no es seguro retorna failure`() = runTest {
        whenever(validator.isContentSafe("bad")).thenReturn(false)

        val result = useCase("bad")

        assert(result.isFailure)
    }

    @Test
    fun `cuando todo es válido retorna success`() = runTest {
        val qr = QrCode(
            id = "10",
            content = "aea",
            type = QrCodeType.TEXT
        )

        whenever(validator.isContentSafe("good")).thenReturn(true)

        val result = useCase("good")

        assert(result.isSuccess)
        assert(result.getOrNull() == qr)
    }
}
