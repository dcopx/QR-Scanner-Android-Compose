package com.uecesar.qrscanner.data.remote.interceptor

import com.uecesar.qrscanner.security.SecurityValidator
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SecurityInterceptor @Inject constructor(
    private val securityValidator: SecurityValidator
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        // Validate request body for malicious content
        request.body?.let { body ->
            // Convert body to string and validate
            val bodyString = body.toString()
            if (!securityValidator.isContentSafe(bodyString)) {
                throw SecurityException("Malicious content detected in request")
            }
        }

        // Add security headers
        val secureRequest = request.newBuilder()
            .addHeader("X-Content-Type-Options", "nosniff")
            .addHeader("X-Frame-Options", "DENY")
            .addHeader("X-XSS-Protection", "1; mode=block")
            .addHeader("Cache-Control", "no-cache, no-store, must-revalidate")
            .build()

        return chain.proceed(secureRequest)
    }
}