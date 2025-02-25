package uk.gov.onelogin.core.network.domain

import kotlin.test.assertEquals
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import uk.gov.android.authentication.login.TokenResponse
import uk.gov.android.network.api.ApiResponse
import uk.gov.android.network.auth.AuthenticationProvider
import uk.gov.android.network.auth.AuthenticationResponse
import uk.gov.android.network.client.GenericHttpClient
import uk.gov.android.network.client.StubHttpClient
import uk.gov.onelogin.core.navigation.data.SignOutRoutes
import uk.gov.onelogin.core.navigation.domain.Navigator
import uk.gov.onelogin.core.tokens.data.TokenRepository
import uk.gov.onelogin.core.tokens.domain.IsAccessTokenExpired

class StsAuthenticationProviderTest {
    private val mockTokenRepository: TokenRepository = mock()
    private val mockIsAccessTokenExpired: IsAccessTokenExpired = mock()
    private val mockNavigator: Navigator = mock()
    private lateinit var stubHttpClient: GenericHttpClient
    private lateinit var provider: AuthenticationProvider

    @Test
    fun `access token is null, failure returned`() =
        runTest {
            setupProvider(ApiResponse.Loading)
            whenever(mockTokenRepository.getTokenResponse()).thenReturn(null)

            val response = provider.fetchBearerToken("scope")

            assertThat("response is Failure", response is AuthenticationResponse.Failure)
            assertEquals(
                "No access token",
                (response as AuthenticationResponse.Failure).error.message
            )
        }

    @Test
    fun `api response is not success, failure returned`() =
        runTest {
            setupProvider(ApiResponse.Failure(500, Exception("error")))
            whenever(mockTokenRepository.getTokenResponse()).thenReturn(
                TokenResponse(
                    tokenType = "type",
                    accessToken = "accessToken",
                    accessTokenExpirationTime = 1L,
                    idToken = "idToken"
                )
            )

            val response = provider.fetchBearerToken("scope")

            assertThat("response is Failure", response is AuthenticationResponse.Failure)
            assertEquals(
                "Failed to fetch service token",
                (response as AuthenticationResponse.Failure).error.message
            )
        }

    @Test
    fun `api response is success but json decode fails, failure returned`() =
        runTest {
            setupProvider(ApiResponse.Success("hello"))
            whenever(mockTokenRepository.getTokenResponse()).thenReturn(
                TokenResponse(
                    tokenType = "type",
                    accessToken = "accessToken",
                    accessTokenExpirationTime = 1L,
                    idToken = "idToken"
                )
            )

            val response = provider.fetchBearerToken("scope")

            assertThat("response is Failure", response is AuthenticationResponse.Failure)
        }

    @Test
    fun `access token is expired, `() =
        runTest {
            setupProvider(ApiResponse.Success("hello"), true)
            whenever(mockTokenRepository.getTokenResponse()).thenReturn(
                TokenResponse(
                    tokenType = "type",
                    accessToken = "accessToken",
                    accessTokenExpirationTime = 1L,
                    idToken = "idToken"
                )
            )

            val response = provider.fetchBearerToken("scope")

            assertThat(
                "response is AccessTokenExpired",
                response is AuthenticationResponse.Failure
            )
            assertEquals(
                "Access token expired",
                (response as AuthenticationResponse.Failure).error.message
            )
            verify(mockNavigator).navigate(SignOutRoutes.Info)
        }

    @Test
    fun `api response is success, success returned`() =
        runTest {
            setupProvider(
                ApiResponse.Success(
                    "{\n" +
                        "    \"access_token\": \"token\",\n" +
                        "    \"token_type\": \"Bearer\",\n" +
                        "    \"expires_in\": 180\n" +
                        "}"
                )
            )
            whenever(mockTokenRepository.getTokenResponse()).thenReturn(
                TokenResponse(
                    tokenType = "type",
                    accessToken = "accessToken",
                    accessTokenExpirationTime = 1L,
                    idToken = "idToken"
                )
            )

            val response = provider.fetchBearerToken("scope")

            assertThat("response is Success", response is AuthenticationResponse.Success)
            assertEquals(
                "token",
                (response as AuthenticationResponse.Success).bearerToken
            )
        }

    private fun setupProvider(
        httpResponse: ApiResponse,
        isAccessTokenExpired: Boolean = false
    ) {
        whenever(mockIsAccessTokenExpired.invoke()).thenReturn(isAccessTokenExpired)
        stubHttpClient = StubHttpClient(httpResponse)
        provider =
            StsAuthenticationProvider(
                "url",
                mockTokenRepository,
                mockIsAccessTokenExpired,
                stubHttpClient,
                mockNavigator
            )
    }
}
