package com.uecesar.qrscanner.api

import com.uecesar.qrscanner.data.remote.api.QrCodeApi
import com.uecesar.qrscanner.domain.model.LoginRequest
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class QrCodeApiTest {

    private val server = MockWebServer()
    private lateinit var api: QrCodeApi

    @Before
    fun setup() {
        server.start()

        api = Retrofit.Builder()
            .baseUrl(server.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(QrCodeApi::class.java)
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun `login devuelve token correcto`() = runTest {
        server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody("""{ "token": "xyz987" }""")
        )

        val response = api.login(LoginRequest("u", "p"))

        assert(response.isSuccessful)
        assert(response.body()?.token == "xyz987")
    }
}
