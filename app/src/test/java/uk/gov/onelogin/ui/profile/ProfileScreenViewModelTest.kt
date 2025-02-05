package uk.gov.onelogin.ui.profile

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
import uk.gov.onelogin.navigation.Navigator
import uk.gov.onelogin.optin.domain.repository.OptInRepository
import uk.gov.onelogin.signOut.SignOutRoutes
import uk.gov.onelogin.tokens.usecases.GetEmail

@OptIn(ExperimentalCoroutinesApi::class)
class ProfileScreenViewModelTest {
    private lateinit var viewModel: ProfileScreenViewModel

    private val mockNavigator: Navigator = mock()
    private val mockGetEmail: GetEmail = mock()
    private val mockOptInRepository: OptInRepository = mock()
    private val testDispatcher = UnconfinedTestDispatcher()

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        whenever(mockOptInRepository.hasAnalyticsOptIn()).thenReturn(flowOf(false))
        viewModel = ProfileScreenViewModel(
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
    fun `optInState is false when repository returns false`() = runTest {
        whenever(mockOptInRepository.hasAnalyticsOptIn()).thenReturn(flowOf(false))

        assertEquals(false, viewModel.optInState.value)
    }

    @Test
    fun `optInState is true when repository returns true`() = runTest {
        whenever(mockOptInRepository.hasAnalyticsOptIn()).thenReturn(flowOf(true))
        viewModel = ProfileScreenViewModel(
            mockOptInRepository,
            mockNavigator,
            mockGetEmail
        )
        assertEquals(true, viewModel.optInState.value)
    }

    @Test
    fun `toggleOptInPreference(true) calls optOut on repository`() = runTest {
        viewModel.toggleOptInPreference(true)

        verify(mockOptInRepository).optOut()
    }

    @Test
    fun `toggleOptInPreference(false) calls optIn on repository`() = runTest {
        viewModel.toggleOptInPreference(false)

        verify(mockOptInRepository).optIn()
    }
}
