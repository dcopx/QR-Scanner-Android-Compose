package com.uecesar.qrscanner.repository

import com.uecesar.qrscanner.data.remote.api.QrCodeApi
import com.uecesar.qrscanner.data.repositoryImpl.QrCodeRepositoryImpl
import com.uecesar.qrscanner.domain.model.LoginResponse
import com.uecesar.qrscanner.security.TokenManager
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import retrofit2.Response

class QrCodeRepositoryImplTest {

    private val api = mock<QrCodeApi>()
    private val tokenManager = mock<TokenManager>()

    private val repo = QrCodeRepositoryImpl(api, tokenManager)

    @Test
    fun `login exitoso retorna true`() = runTest {
        whenever(api.login(any())).thenReturn(
            Response.success(LoginResponse(token = "abc123"))
        )

        val ok = repo.login("user", "pass")

        assert(ok)
    }

    @Test
    fun `login fallido retorna false`() = runTest {
        whenever(api.login(any())).thenReturn(
            Response.error(401, "Unauthorized".toResponseBody())
        )

        val ok = repo.login("user", "wrong")

        assert(!ok)
    }
}
