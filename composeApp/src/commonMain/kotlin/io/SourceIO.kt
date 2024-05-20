package io

import kotlinx.serialization.Serializable

@Serializable
data class SourceIO(
    val name: String,
    val url: String
)