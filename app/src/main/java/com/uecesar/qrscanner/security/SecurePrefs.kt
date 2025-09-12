package com.uecesar.qrscanner.security

import android.content.Context
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import android.util.Base64
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey

class SecurePrefs(private val context: Context) {
    private val aead by lazy { CryptoManager.getAead(context) }
    private val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
    private val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")

    suspend fun saveAccessToken(token: String) {
        saveEncrypted(ACCESS_TOKEN_KEY, token)
    }

    suspend fun saveRefreshToken(token: String) {
        saveEncrypted(REFRESH_TOKEN_KEY, token)
    }

    val accessTokenFlow: Flow<String?> = readEncrypted(ACCESS_TOKEN_KEY)
    val refreshTokenFlow: Flow<String?> = readEncrypted(REFRESH_TOKEN_KEY)

    suspend fun clearAccessToken() {
        context.dataStore.edit { it.remove(ACCESS_TOKEN_KEY) }
    }

    suspend fun clearRefreshToken() {
        context.dataStore.edit { it.remove(REFRESH_TOKEN_KEY) }
    }

    // Helpers
    private suspend fun saveEncrypted(key: Preferences.Key<String>, value: String) {
        val encrypted = aead.encrypt(value.toByteArray(), null)
        val base64 = Base64.encodeToString(encrypted, Base64.DEFAULT)
        context.dataStore.edit { it[key] = base64 }
    }

    private fun readEncrypted(key: Preferences.Key<String>): Flow<String?> =
        context.dataStore.data.map { prefs ->
            prefs[key]?.let {
                try {
                    val decrypted = aead.decrypt(Base64.decode(it, Base64.DEFAULT), null)
                    String(decrypted)
                } catch (e: Exception) {
                    null
                }
            }
        }
}
