package uk.gov.onelogin.core.tokens.domain.retrieve

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever
import uk.gov.android.authentication.login.TokenResponse
import uk.gov.onelogin.core.tokens.data.TokenRepository

class GetEmailImplTest {
    private val expectedEmail = "email@mail.com"
    private val idTokenWithEmail =
        "eyJhbGciOiJIUzI1NiJ9" +
            ".eyJlbWFpbCI6ImVtYWlsQG1haWwuY29tIn0" + // payload contains "email": "email@mail.com"
            ".mHuqqrjGNsVpzm-8jiZ8VnlWuAVSlexyjDsOX7YDB6Q"
    private val idTokenWithoutEmail =
        "eyJhbGciOiJIUzI1NiJ9" +
            ".e30." + // no email in the payload
            "ZRrHA1JJJW8opsbCGfG_HACGpVUMN_a9IV7pAx_Zmeo"
    private val tokenResponse: TokenResponse = mock()
    private val tokenRepository: TokenRepository = mock()

    val sut = GetEmailImpl(tokenRepository)

    @BeforeEach
    fun setUp() {
        whenever(tokenResponse.idToken).thenReturn(idTokenWithEmail)
        whenever(tokenRepository.getTokenResponse()).thenReturn(tokenResponse)
    }

    @Test
    fun successScenario() {
        // Given id token contains email
        whenever(tokenResponse.idToken).thenReturn(idTokenWithEmail)
        whenever(tokenRepository.getTokenResponse()).thenReturn(tokenResponse)

        val emailResponse = sut.invoke()
        assertEquals(expectedEmail, emailResponse)
    }

    @Test
    fun missingEmailScenario() {
        // Given id token is missing the email
        whenever(tokenResponse.idToken).thenReturn(idTokenWithoutEmail)
        whenever(tokenRepository.getTokenResponse()).thenReturn(tokenResponse)

        val emailResponse = sut.invoke()
        assertEquals(null, emailResponse)
    }

    @Test
    fun missingTokenScenario() {
        // Given token is null
        whenever(tokenRepository.getTokenResponse()).thenReturn(null)

        val emailResponse = sut.invoke()
        assertEquals(null, emailResponse)
    }

    @Test
    fun missingIdTokenScenario() {
        // Given id token is null
        whenever(tokenResponse.idToken).thenReturn(null)

        val emailResponse = sut.invoke()
        assertEquals(null, emailResponse)
    }
}
