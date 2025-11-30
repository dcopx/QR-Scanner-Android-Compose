package com.uecesar.qrscanner.utils

import com.uecesar.qrscanner.security.SecurityValidator
import org.junit.Test

class SecurityValidatorTest {

    private val validator = SecurityValidator()

    @Test
    fun `detecta contenido potencialmente malicioso`() {
        assert(!validator.isContentSafe("DROP TABLE USERS"))
        assert(!validator.isContentSafe("<script>alert(1)</script>"))
    }

    @Test
    fun `acepta contenido seguro`() {
        assert(validator.isContentSafe("HolaMundo123"))
        assert(validator.isContentSafe("https://example.com"))
    }
}
