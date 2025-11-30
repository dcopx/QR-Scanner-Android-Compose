package com.uecesar.qrscanner.data.remote.api

import com.uecesar.qrscanner.domain.model.LoginRequest
import com.uecesar.qrscanner.domain.model.LoginResponse
import com.uecesar.qrscanner.domain.model.ApiResponse
import com.uecesar.qrscanner.domain.model.PaginatedResponse
import com.uecesar.qrscanner.domain.model.QrCode
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface QrCodeApi {

    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<LoginResponse>
    @GET("api/v1/qr-codes")
    suspend fun getAllQrCodes(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20,
        @Header("Authorization") token: String
    ): Response<PaginatedResponse<QrCode>>

    @GET("api/v1/qr-codes/{id}")
    suspend fun getQrCodeById(
        @Path("id") id: String,
        @Header("Authorization") token: String
    ): Response<ApiResponse<QrCode>>

    @POST("api/v1/qr-codes")
    suspend fun createQrCode(
        @Body qrCode: QrCode,
        @Header("Authorization") token: String
    ): Response<ApiResponse<QrCode>>

    @PUT("api/v1/qr-codes/{id}")
    suspend fun updateQrCode(
        @Path("id") id: String,
        @Body qrCode: QrCode,
        @Header("Authorization") token: String
    ): Response<ApiResponse<QrCode>>

    @DELETE("api/v1/qr-codes/{id}")
    suspend fun deleteQrCode(
        @Path("id") id: String,
        @Header("Authorization") token: String
    ): Response<ApiResponse<Unit>>

    @DELETE("api/v1/qr-codes")
    suspend fun deleteAllQrCodes(
        @Header("Authorization") token: String
    ): Response<ApiResponse<Unit>>

    @GET("api/v1/qr-codes/search")
    suspend fun searchQrCodes(
        @Query("q") query: String,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20,
        @Header("Authorization") token: String
    ): Response<PaginatedResponse<QrCode>>
}