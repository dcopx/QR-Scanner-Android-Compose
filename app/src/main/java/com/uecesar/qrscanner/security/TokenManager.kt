package com.uecesar.qrscanner.security

import com.uecesar.qrscanner.data.local.SecurePrefs
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenManager @Inject constructor(
    private val securePrefs: SecurePrefs
) {
    suspend fun saveTokens(accessToken: String, refreshToken: String? = null) {
        securePrefs.saveAccessToken(accessToken)
        refreshToken?.let {
            securePrefs.saveRefreshToken(it)
        }
    }

    val accessToken: Flow<String?> = securePrefs.accessTokenFlow
    val refreshToken: Flow<String?> = securePrefs.refreshTokenFlow

    suspend fun clearTokens() {
        securePrefs.clearAccessToken()
        securePrefs.clearRefreshToken()
    }

    fun isLoggedIn(): Flow<Boolean> = accessToken.map { it != null }
}
