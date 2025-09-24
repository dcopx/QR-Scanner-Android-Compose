package com.uecesar.qrscanner.domain.model

import com.uecesar.qrscanner.presentation.ui.enumerable.QrCodeType
import kotlinx.serialization.Serializable

@Serializable
sealed class QrCodeContent {
    abstract fun toRawString(): String
    abstract fun getType(): QrCodeType

    @Serializable
    data class Text(val text: String) : QrCodeContent() {
        override fun toRawString() = text
        override fun getType() = QrCodeType.TEXT
    }

    @Serializable
    data class Email(
        val email: String,
        val subject: String = "",
        val body: String = ""
    ) : QrCodeContent() {
        override fun toRawString() = "mailto:$email?subject=$subject&body=$body"
        override fun getType() = QrCodeType.EMAIL
    }

    @Serializable
    data class Phone(val number: String) : QrCodeContent() {
        override fun toRawString() = "tel:$number"
        override fun getType() = QrCodeType.PHONE
    }

    @Serializable
    data class Url(val url: String) : QrCodeContent() {
        override fun toRawString() = url
        override fun getType() = QrCodeType.URL
    }

}
