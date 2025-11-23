package com.uecesar.qrscanner.domain.useCase

import android.graphics.Bitmap
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import com.uecesar.qrscanner.domain.model.QrCode
import com.uecesar.qrscanner.domain.model.QrCodeContent
import com.uecesar.qrscanner.domain.repository.QrCodeRepository
import com.uecesar.qrscanner.security.SecurityValidator
import javax.inject.Inject
import javax.inject.Singleton
import androidx.core.graphics.createBitmap
import androidx.core.graphics.set

@Singleton
class GenerateQrCodeUseCase @Inject constructor(
    private val repository: QrCodeRepository,
    private val securityValidator: SecurityValidator
) {
//    suspend operator fun invoke(content: QrCodeContent): Result<QrCode> {
//        return try {
//            val rawContent = content.toRawString()
//
//            if (!securityValidator.isContentSafe(rawContent)) {
//                return Result.failure(SecurityException("Invalid content for QR generation"))
//            }
//
//            val qrCode = QrCode(
//                content = rawContent,
//                type = content.getType(),
//                isGenerated = true
//            )
//
//            // Save to backend
//            repository.createQrCode(qrCode)
//        } catch (e: Exception) {
//            Result.failure(e)
//        }
//    }
    operator fun invoke(content: QrCodeContent): Result<Bitmap> {
        return runCatching {
            val writer = QRCodeWriter()
            val bitMatrix = writer.encode(
                content.toRawString(),
                BarcodeFormat.QR_CODE,
                512,
                512
            )

            val width = bitMatrix.width
            val height = bitMatrix.height
            val bmp = createBitmap(width, height, Bitmap.Config.RGB_565)

            for (x in 0 until width) {
                for (y in 0 until height) {
                    bmp[x, y] = if (bitMatrix[x, y]) Color.Black.toArgb() else Color.White.toArgb()
                }
            }

            bmp
        }
    }
}