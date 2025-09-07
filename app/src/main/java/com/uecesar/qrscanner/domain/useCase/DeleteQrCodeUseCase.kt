package com.uecesar.qrscanner.domain.useCase

import com.uecesar.qrscanner.domain.repository.QrCodeRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeleteQrCodeUseCase @Inject constructor(
    private val repository: QrCodeRepository
) {
    suspend operator fun invoke(id: String): Result<Unit> {
        return repository.deleteQrCode(id)
    }
}