package uk.gov.onelogin.network

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@Serializable
@OptIn(ExperimentalSerializationApi::class)
data class TokenResponse(
    @JsonNames("access_token")
    val token: String,
    @JsonNames("token_type")
    val tokenType: String,
    @JsonNames("expires_in")
    val expiresIn: Int
)
