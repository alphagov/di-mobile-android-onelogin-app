package uk.gov.onelogin.features.login.ui.biooptin

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import uk.gov.onelogin.core.biometrics.data.BiometricPreference
import uk.gov.onelogin.core.biometrics.domain.BiometricPreferenceHandler
import uk.gov.onelogin.core.navigation.data.MainNavRoutes
import uk.gov.onelogin.core.navigation.domain.Navigator
import uk.gov.onelogin.core.tokens.data.initialise.AutoInitialiseSecureStore
import uk.gov.onelogin.features.extensions.CoroutinesTestExtension
import uk.gov.onelogin.features.extensions.InstantExecutorExtension
import uk.gov.onelogin.features.login.ui.signin.biooptin.BioOptInViewModel

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(InstantExecutorExtension::class, CoroutinesTestExtension::class)
class BioOptInViewModelTest {
    private val mockBioPrefHandler: BiometricPreferenceHandler = mock()
    private val mockAutoInitialiseSecureStore: AutoInitialiseSecureStore = mock()
    private val mockNavigator: Navigator = mock()

    private val viewModel =
        BioOptInViewModel(
            mockBioPrefHandler,
            mockAutoInitialiseSecureStore,
            mockNavigator
        )

    @Test
    fun `use biometrics`() =
        runTest {
            viewModel.useBiometrics()

            verify(mockAutoInitialiseSecureStore).initialise()
            verify(mockBioPrefHandler).setBioPref(BiometricPreference.BIOMETRICS)
            verify(mockNavigator).navigate(MainNavRoutes.Start, true)
        }

    @Test
    fun `not use biometrics`() =
        runTest {
            viewModel.doNotUseBiometrics()

            verify(mockAutoInitialiseSecureStore).initialise()
            verify(mockBioPrefHandler).setBioPref(BiometricPreference.PASSCODE)
            verify(mockNavigator).navigate(MainNavRoutes.Start, true)
        }
}
