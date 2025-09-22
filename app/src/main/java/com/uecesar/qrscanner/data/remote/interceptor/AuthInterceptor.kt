package com.uecesar.qrscanner.data.remote.interceptor

import com.uecesar.qrscanner.security.TokenManager
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        // Skip auth for login/register endpoints
        if (originalRequest.url.pathSegments.contains("auth")) {
            return chain.proceed(originalRequest)
        }

        val token = tokenManager.accessToken

        val authenticatedRequest = if (token != null) {
            originalRequest.newBuilder()
                .header("Authorization", "Bearer $token")
                .build()
        } else {
            originalRequest
        }

        val response = chain.proceed(authenticatedRequest)

        // Handle 401 Unauthorized
        if (response.code == 401) {
            //tokenManager.clearTokens()
            // Redirect to login or refresh token
        }

        return response
    }
}