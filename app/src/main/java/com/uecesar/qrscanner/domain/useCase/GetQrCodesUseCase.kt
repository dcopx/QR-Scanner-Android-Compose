package com.uecesar.qrscanner.domain.useCase

import com.uecesar.qrscanner.domain.model.PaginatedResponse
import com.uecesar.qrscanner.domain.model.QrCode
import com.uecesar.qrscanner.domain.repository.QrCodeRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetQrCodesUseCase @Inject constructor(
    private val repository: QrCodeRepository
) {
    suspend operator fun invoke(page: Int = 0, size: Int = 20): Result<PaginatedResponse<QrCode>> {
        return repository.getAllQrCodes(page, size)
    }
}