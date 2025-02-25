package uk.gov.onelogin.features.settings.ui

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import uk.gov.onelogin.core.navigation.data.SignOutRoutes
import uk.gov.onelogin.core.navigation.domain.Navigator
import uk.gov.onelogin.core.tokens.domain.retrieve.GetEmail
import uk.gov.onelogin.features.optin.data.OptInRepository

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsScreenViewModelTest {
    private lateinit var viewModel: SettingsScreenViewModel

    private val mockNavigator: Navigator = mock()
    private val mockGetEmail: GetEmail = mock()
    private val mockOptInRepository: OptInRepository = mock()
    private val testDispatcher = UnconfinedTestDispatcher()

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        whenever(mockOptInRepository.hasAnalyticsOptIn()).thenReturn(flowOf(false))
        viewModel =
            SettingsScreenViewModel(
                mockOptInRepository,
                mockNavigator,
                mockGetEmail
            )
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `email is empty when getEmail returns null`() {
        whenever(mockGetEmail.invoke()).thenReturn(null)
        setup()

        assert(viewModel.email.isEmpty())
    }

    @Test
    fun `email is given when getEmail returns a value`() {
        whenever(mockGetEmail.invoke()).thenReturn("test")
        setup()

        assertEquals("test", viewModel.email)
    }

    @Test
    fun `goToSignOut() correctly navigates to sign out`() {
        viewModel.goToSignOut()

        verify(mockNavigator).navigate(SignOutRoutes.Start, false)
    }

    @Test
    fun `optInState is false when repository returns false`() =
        runTest {
            whenever(mockOptInRepository.hasAnalyticsOptIn()).thenReturn(flowOf(false))

            assertEquals(false, viewModel.optInState.value)
        }

    @Test
    fun `optInState is true when repository returns true`() =
        runTest {
            whenever(mockOptInRepository.hasAnalyticsOptIn()).thenReturn(flowOf(true))
            viewModel =
                SettingsScreenViewModel(
                    mockOptInRepository,
                    mockNavigator,
                    mockGetEmail
                )
            assertEquals(true, viewModel.optInState.value)
        }

    @Test
    fun `toggleOptInPreference calls optOut on repository when state is true`() =
        runTest {
            whenever(mockOptInRepository.hasAnalyticsOptIn()).thenReturn(flowOf(true))
            viewModel =
                SettingsScreenViewModel(
                    mockOptInRepository,
                    mockNavigator,
                    mockGetEmail
                )
            assertEquals(true, viewModel.optInState.value)

            viewModel.toggleOptInPreference()

            verify(mockOptInRepository).optOut()
        }

    @Test
    fun `toggleOptInPreference calls optOut on repository when state is false`() =
        runTest {
            whenever(mockOptInRepository.hasAnalyticsOptIn()).thenReturn(flowOf(false))
            viewModel =
                SettingsScreenViewModel(
                    mockOptInRepository,
                    mockNavigator,
                    mockGetEmail
                )
            assertEquals(false, viewModel.optInState.value)

            viewModel.toggleOptInPreference()

            verify(mockOptInRepository).optIn()
        }
}
