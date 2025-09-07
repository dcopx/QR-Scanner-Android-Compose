package com.uecesar.qrscanner.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class PaginatedResponse<T>(
    val success: Boolean,
    val data: List<T>,
    val pagination: Pagination,
    val message: String? = null
)
