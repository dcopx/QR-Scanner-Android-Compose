package com.uecesar.qrscanner.domain.useCase

import com.uecesar.qrscanner.domain.model.QrCode
import com.uecesar.qrscanner.domain.model.QrCodeContent
import com.uecesar.qrscanner.domain.repository.QrCodeRepository
import com.uecesar.qrscanner.security.SecurityValidator
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GenerateQrCodeUseCase @Inject constructor(
    private val repository: QrCodeRepository,
    private val securityValidator: SecurityValidator
) {
    suspend operator fun invoke(content: QrCodeContent): Result<QrCode> {
        return try {
            val rawContent = content.toRawString()

            if (!securityValidator.isContentSafe(rawContent)) {
                return Result.failure(SecurityException("Invalid content for QR generation"))
            }

            val qrCode = QrCode(
                content = rawContent,
                type = content.getType(),
                isGenerated = true
            )

            // Save to backend
            repository.createQrCode(qrCode)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}