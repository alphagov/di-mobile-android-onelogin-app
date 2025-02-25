package uk.gov.onelogin.core.network.domain

import kotlin.test.Test
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertEquals

class TokenApiResponseTest {
    @Test
    fun testSerializationAndDeserialization() {
        val tokenApiResponse =
            TokenApiResponse(
                token = "test_token",
                tokenType = "Bearer",
                expiresIn = 3600
            )

        val jsonString = Json.encodeToString(TokenApiResponse.serializer(), tokenApiResponse)

        val deserializedApiResponse =
            Json.decodeFromString(
                TokenApiResponse.serializer(),
                jsonString
            )

        assertEquals(tokenApiResponse, deserializedApiResponse)
    }
}
