package com.uecesar.qrscanner.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Pagination(
    val page: Int,
    val size: Int,
    val total: Long,
    val totalPages: Int
)
