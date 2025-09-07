package com.uecesar.qrscanner.domain.useCase

import com.uecesar.qrscanner.domain.model.QrCode
import com.uecesar.qrscanner.domain.repository.QrCodeRepository
import com.uecesar.qrscanner.security.QrContentParser
import com.uecesar.qrscanner.security.SecurityValidator
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ScanQrCodeUseCase @Inject constructor(
    private val repository: QrCodeRepository,
    private val contentParser: QrContentParser,
    private val securityValidator: SecurityValidator
) {
    suspend operator fun invoke(rawContent: String): Result<QrCode> {
        return try {
            // Validate content for security
            if (!securityValidator.isContentSafe(rawContent)) {
                return Result.failure(SecurityException("Potentially malicious content detected"))
            }

            val sanitizedContent = securityValidator.sanitizeContent(rawContent)
            val type = contentParser.parseType(sanitizedContent)

            val qrCode = QrCode(
                content = sanitizedContent,
                type = type,
                isGenerated = false
            )

            // Save to backend
            repository.createQrCode(qrCode)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}