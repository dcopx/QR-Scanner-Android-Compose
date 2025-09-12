package com.uecesar.qrscanner.domain.repository

import com.uecesar.qrscanner.domain.model.PaginatedResponse
import com.uecesar.qrscanner.domain.model.QrCode

interface QrCodeRepository {

    suspend fun login(username: String, password: String): Boolean
    suspend fun getAllQrCodes(page: Int = 0, size: Int = 20): Result<PaginatedResponse<QrCode>>
    suspend fun getQrCodeById(id: String): Result<QrCode>
    suspend fun createQrCode(qrCode: QrCode): Result<QrCode>
    suspend fun updateQrCode(id: String, qrCode: QrCode): Result<QrCode>
    suspend fun deleteQrCode(id: String): Result<Unit>
    suspend fun deleteAllQrCodes(): Result<Unit>
    suspend fun searchQrCodes(query: String, page: Int = 0, size: Int = 20): Result<PaginatedResponse<QrCode>>
}