package com.uecesar.qrscanner.security

import android.util.Patterns
import java.net.URL
import java.util.regex.Pattern
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SecurityValidator @Inject constructor() {

    private val maliciousPatterns = listOf(
        Pattern.compile("<script[^>]*>.*?</script>", Pattern.CASE_INSENSITIVE or Pattern.DOTALL),
        Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE),
        Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE),
        Pattern.compile("data:text/html", Pattern.CASE_INSENSITIVE),
        Pattern.compile("\\bon\\w+=", Pattern.CASE_INSENSITIVE),
        Pattern.compile("eval\\s*\\(", Pattern.CASE_INSENSITIVE),
        Pattern.compile("expression\\s*\\(", Pattern.CASE_INSENSITIVE)
    )

    private val allowedUrlSchemes = listOf("https", "mailto", "tel", "sms", "geo")
    private val maxContentLength = 4296 // QR code maximum capacity

    fun isContentSafe(content: String): Boolean {
        // Check length
        if (content.length > maxContentLength) return false

        // Check for malicious patterns
        maliciousPatterns.forEach { pattern ->
            if (pattern.matcher(content).find()) {
                return false
            }
        }

        // Additional checks for URLs
        if (isUrl(content)) {
            return isUrlSafe(content)
        }

        // Check for SQL injection patterns
        if (containsSqlInjection(content)) return false

        // Check for command injection
        if (containsCommandInjection(content)) return false

        return true
    }

    fun sanitizeContent(content: String): String {
        var sanitized = content.trim()

        // Remove potential HTML tags
        sanitized = sanitized.replace(Regex("<[^>]+>"), "")

        // Decode HTML entities
        sanitized = sanitized
            .replace("&lt;", "<")
            .replace("&gt;", ">")
            .replace("&amp;", "&")
            .replace("&quot;", "\"")
            .replace("&#39;", "'")
            .replace("&#x27;", "'")
            .replace("&#x2F;", "/")

        // Remove null bytes
        sanitized = sanitized.replace("\u0000", "")

        return sanitized.take(maxContentLength)
    }

    private fun isUrl(content: String): Boolean {
        return Patterns.WEB_URL.matcher(content).matches() ||
                content.startsWith("http://") ||
                content.startsWith("https://")
    }

    private fun isUrlSafe(url: String): Boolean {
        return try {
            val urlObj = URL(url)
            val scheme = urlObj.protocol.lowercase()

            // Check allowed schemes
            if (!allowedUrlSchemes.contains(scheme)) return false

            // Check for suspicious domains
            val host = urlObj.host.lowercase()
            if (host.contains("localhost") ||
                host.contains("127.0.0.1") ||
                host.contains("0.0.0.0")) {
                return false
            }

            true
        } catch (e: Exception) {
            false
        }
    }

    private fun containsSqlInjection(content: String): Boolean {
        val sqlPatterns = listOf(
            "(?i)(union|select|insert|update|delete|drop|create|alter|exec|execute)",
            "(?i)(or|and)\\s+\\d+\\s*=\\s*\\d+",
            "(?i)'\\s*(or|and)\\s*'\\w*'\\s*=\\s*'\\w*'",
            "(?i)(--|#|/\\*)"
        )

        return sqlPatterns.any { Pattern.compile(it).matcher(content).find() }
    }

    private fun containsCommandInjection(content: String): Boolean {
        val commandPatterns = listOf(
            // Operadores de shell
            "(;|&&|\\|\\|)",
            // Subshel
            "\\$\\(",
            // Backtick
            "`",
            // Redirecciones
            "(>|<|>>|<<)",
            // Separadores sospechosos tipo pipe
            "(?<![A-Za-z0-9])\\|(?![A-Za-z0-9])"
        )
        return commandPatterns.any { Pattern.compile(it).matcher(content).find() }
    }
}