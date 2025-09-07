package com.uecesar.qrscanner.di

import com.uecesar.qrscanner.data.remote.api.QrCodeApi
import com.uecesar.qrscanner.data.repositoryImpl.QrCodeRepositoryImpl
import com.uecesar.qrscanner.domain.repository.QrCodeRepository
import com.uecesar.qrscanner.security.TokenManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideQrCodeRepository(
        api: QrCodeApi,
        tokenManager: TokenManager
    ): QrCodeRepository {
        return QrCodeRepositoryImpl(api, tokenManager)
    }
}