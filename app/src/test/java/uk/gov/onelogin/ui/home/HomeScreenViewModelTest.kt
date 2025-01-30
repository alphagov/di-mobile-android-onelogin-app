package uk.gov.onelogin.ui.home

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import uk.gov.android.network.client.GenericHttpClient
import uk.gov.onelogin.extensions.CoroutinesTestExtension
import uk.gov.onelogin.extensions.InstantExecutorExtension
import uk.gov.onelogin.navigation.Navigator
import uk.gov.onelogin.repositiories.TokenRepository
import uk.gov.onelogin.tokens.usecases.GetEmail

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(InstantExecutorExtension::class, CoroutinesTestExtension::class)
class HomeScreenViewModelTest {
    private val mockNavigator: Navigator = mock()
    private val tokenRepository: TokenRepository = mock()
    private val getEmail: GetEmail = mock()
    private val httpClient: GenericHttpClient = mock()

    private val viewModel by lazy {
        HomeScreenViewModel(
            httpClient,
            mockNavigator,
            tokenRepository,
            getEmail
        )
    }

    @Test
    fun emailIsReturned() {
        whenever(getEmail()).thenReturn("test")

        assertEquals("test", viewModel.email)
    }

    @Test
    fun emailIsEmptyWhenNoEmailReturned() {
        whenever(getEmail()).thenReturn(null)

        assertEquals("", viewModel.email)
    }

    @Test
    fun openDevPanel() {
        viewModel.openDevPanel()

        verify(mockNavigator).openDeveloperPanel()
    }
}
