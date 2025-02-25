package uk.gov.onelogin.core.tokens.data

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import uk.gov.android.authentication.login.TokenResponse

class TokenRepositoryTest {
    private lateinit var repo: TokenRepository

    @BeforeEach
    fun setup() {
        repo = TokenRepositoryImpl()
    }

    @Test
    fun `check set and retrieve`() {
        val testResponse =
            TokenResponse(
                tokenType = "test",
                accessToken = "test",
                accessTokenExpirationTime = 1L,
                idToken = "test"
            )
        repo.setTokenResponse(testResponse)

        assertEquals(testResponse, repo.getTokenResponse())
    }

    @Test
    fun `check null on retrieve`() {
        assertNull(repo.getTokenResponse())
    }

    @Test
    fun `test clearTokenResponse`() {
        // given a token is saved in the repository
        val testResponse =
            TokenResponse(
                tokenType = "test",
                accessToken = "test",
                accessTokenExpirationTime = 1L,
                idToken = "test"
            )
        repo.setTokenResponse(testResponse)

        // when clearTokenResponse called
        repo.clearTokenResponse()

        // repository is cleared
        assertNull(repo.getTokenResponse())
    }
}
