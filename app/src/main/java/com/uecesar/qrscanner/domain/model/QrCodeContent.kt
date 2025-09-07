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

    @Serializable
    data class Wifi(
        val ssid: String,
        val password: String,
        val security: String
    ) : QrCodeContent() {
        override fun toRawString() = "WIFI:T:$security;S:$ssid;P:$password;;"
        override fun getType() = QrCodeType.WIFI
    }

    @Serializable
    data class Contact(
        val name: String,
        val phone: String,
        val email: String
    ) : QrCodeContent() {
        override fun toRawString() = "BEGIN:VCARD\nVERSION:3.0\nFN:$name\nTEL:$phone\nEMAIL:$email\nEND:VCARD"
        override fun getType() = QrCodeType.CONTACT
    }

    @Serializable
    data class Location(
        val latitude: Double,
        val longitude: Double
    ) : QrCodeContent() {
        override fun toRawString() = "geo:$latitude,$longitude"
        override fun getType() = QrCodeType.LOCATION
    }

    @Serializable
    data class Sms(val number: String, val message: String) : QrCodeContent() {
        override fun toRawString() = "sms:$number?body=$message"
        override fun getType() = QrCodeType.SMS
    }
}
