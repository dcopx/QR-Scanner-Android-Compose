package com.uecesar.qrscanner.data.repositoryImpl

import com.uecesar.qrscanner.data.remote.api.QrCodeApi
import com.uecesar.qrscanner.domain.model.PaginatedResponse
import com.uecesar.qrscanner.domain.model.QrCode
import com.uecesar.qrscanner.domain.repository.QrCodeRepository
import com.uecesar.qrscanner.security.TokenManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QrCodeRepositoryImpl @Inject constructor(
    private val api: QrCodeApi,
    private val tokenManager: TokenManager
) : QrCodeRepository {

    override suspend fun getAllQrCodes(page: Int, size: Int): Result<PaginatedResponse<QrCode>> {
        return try {
            val token = tokenManager.getAccessToken() ?: return Result.failure(
                Exception("Authentication required")
            )

            val response = api.getAllQrCodes(page, size, "Bearer $token")

            if (response.isSuccessful) {
                response.body()?.let { body ->
                    Result.success(body)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                Result.failure(Exception("HTTP ${response.code()}: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getQrCodeById(id: String): Result<QrCode> {
        return try {
            val token = tokenManager.getAccessToken() ?: return Result.failure(
                Exception("Authentication required")
            )

            val response = api.getQrCodeById(id, "Bearer $token")

            if (response.isSuccessful) {
                response.body()?.data?.let { qrCode ->
                    Result.success(qrCode)
                } ?: Result.failure(Exception("QR Code not found"))
            } else {
                Result.failure(Exception("HTTP ${response.code()}: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun createQrCode(qrCode: QrCode): Result<QrCode> {
        return try {
            val token = tokenManager.getAccessToken() ?: return Result.failure(
                Exception("Authentication required")
            )

            val response = api.createQrCode(qrCode, "Bearer $token")

            if (response.isSuccessful) {
                response.body()?.data?.let { createdQrCode ->
                    Result.success(createdQrCode)
                } ?: Result.failure(Exception("Failed to create QR Code"))
            } else {
                Result.failure(Exception("HTTP ${response.code()}: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateQrCode(id: String, qrCode: QrCode): Result<QrCode> {
        return try {
            val token = tokenManager.getAccessToken() ?: return Result.failure(
                Exception("Authentication required")
            )

            val response = api.updateQrCode(id, qrCode, "Bearer $token")

            if (response.isSuccessful) {
                response.body()?.data?.let { updatedQrCode ->
                    Result.success(updatedQrCode)
                } ?: Result.failure(Exception("Failed to update QR Code"))
            } else {
                Result.failure(Exception("HTTP ${response.code()}: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteQrCode(id: String): Result<Unit> {
        return try {
            val token = tokenManager.getAccessToken() ?: return Result.failure(
                Exception("Authentication required")
            )

            val response = api.deleteQrCode(id, "Bearer $token")

            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("HTTP ${response.code()}: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteAllQrCodes(): Result<Unit> {
        return try {
            val token = tokenManager.getAccessToken() ?: return Result.failure(
                Exception("Authentication required")
            )

            val response = api.deleteAllQrCodes("Bearer $token")

            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("HTTP ${response.code()}: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun searchQrCodes(query: String, page: Int, size: Int): Result<PaginatedResponse<QrCode>> {
        return try {
            val token = tokenManager.getAccessToken() ?: return Result.failure(
                Exception("Authentication required")
            )

            val response = api.searchQrCodes(query, page, size, "Bearer $token")

            if (response.isSuccessful) {
                response.body()?.let { body ->
                    Result.success(body)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                Result.failure(Exception("HTTP ${response.code()}: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}