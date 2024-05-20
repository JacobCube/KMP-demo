package io

import kotlinx.serialization.Serializable

@Serializable
data class PagingInfo(
    /** The length of the response */
    val count: Int? = null,
    /** The length of the response */
    val pages: Int? = null,
    /** The length of the response */
    val next: String? = null,
    /** The length of the response */
    val prev: String? = null
)