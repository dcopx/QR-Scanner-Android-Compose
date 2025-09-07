package com.uecesar.qrscanner.security

import android.util.Patterns
import com.uecesar.qrscanner.presentation.ui.enumerable.QrCodeType
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QrContentParser @Inject constructor() {

    fun parseType(content: String): QrCodeType {
        return when {
            content.startsWith("mailto:", ignoreCase = true) -> QrCodeType.EMAIL
            content.startsWith("tel:", ignoreCase = true) -> QrCodeType.PHONE
            content.startsWith("sms:", ignoreCase = true) -> QrCodeType.SMS
            content.startsWith("geo:", ignoreCase = true) -> QrCodeType.LOCATION
            content.startsWith("WIFI:", ignoreCase = true) -> QrCodeType.WIFI
            content.contains("BEGIN:VCARD", ignoreCase = true) -> QrCodeType.CONTACT
            isUrl(content) -> QrCodeType.URL
            else -> QrCodeType.TEXT
        }
    }

    private fun isUrl(content: String): Boolean {
        return Patterns.WEB_URL.matcher(content).matches() ||
                content.startsWith("http://", ignoreCase = true) ||
                content.startsWith("https://", ignoreCase = true)
    }
}