package uk.gov.onelogin.features.signout.ui

import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.espresso.Espresso
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import uk.gov.android.featureflags.FeatureFlags
import uk.gov.android.featureflags.InMemoryFeatureFlags
import uk.gov.android.onelogin.core.R
import uk.gov.logging.api.analytics.logging.AnalyticsLogger
import uk.gov.logging.api.v3dot1.logger.logEventV3Dot1
import uk.gov.onelogin.core.navigation.data.ErrorRoutes
import uk.gov.onelogin.core.navigation.data.LoginRoutes
import uk.gov.onelogin.core.navigation.domain.Navigator
import uk.gov.onelogin.core.ui.pages.loading.LOADING_SCREEN_PROGRESS_INDICATOR
import uk.gov.onelogin.core.ui.pages.loading.LoadingScreenAnalyticsViewModel
import uk.gov.onelogin.features.FragmentActivityTestCase
import uk.gov.onelogin.features.featureflags.data.CriOrchestratorFeatureFlag
import uk.gov.onelogin.features.featureflags.data.WalletFeatureFlag
import uk.gov.onelogin.features.signout.domain.SignOutError
import uk.gov.onelogin.features.signout.domain.SignOutUseCase

class SignOutScreenTest : FragmentActivityTestCase() {
    private lateinit var analytics: AnalyticsLogger
    private lateinit var loadingAnalyticsVM: LoadingScreenAnalyticsViewModel
    private lateinit var navigator: Navigator
    private lateinit var signOutUseCase: SignOutUseCase
    private lateinit var featureFlags: FeatureFlags
    private lateinit var viewModel: SignOutViewModel
    private lateinit var analyticsViewModel: SignOutAnalyticsViewModel
    private lateinit var title: SemanticsMatcher
    private lateinit var button: SemanticsMatcher
    private lateinit var closeButton: SemanticsMatcher

    @Before
    fun setup() {
        analytics = mock()
        loadingAnalyticsVM = LoadingScreenAnalyticsViewModel(context, analytics)
        navigator = mock()
        signOutUseCase = mock()
        analyticsViewModel = SignOutAnalyticsViewModel(context, analytics)
        title = hasText(resources.getString(R.string.app_signOutConfirmationTitle))
        button = hasText(resources.getString(R.string.app_signOutAndDeleteAppDataButton))
        closeButton = hasContentDescription("Close")
    }

    @Test
    fun verifyScreenDisplayedWallet() {
        featureFlags = InMemoryFeatureFlags(
            setOf(WalletFeatureFlag.ENABLED, CriOrchestratorFeatureFlag.ENABLED)
        )
        viewModel = SignOutViewModel(navigator, signOutUseCase, featureFlags)
        composeTestRule.setContent {
            SignOutScreen(viewModel, analyticsViewModel, loadingAnalyticsVM)
        }
        composeTestRule.onNode(title).assertIsDisplayed()
        verify(analytics).logEventV3Dot1(
            SignOutAnalyticsViewModel.makeSignOutWalletViewEvent(
                context
            )
        )
    }

    @Test
    fun verifyScreenDisplayedNoWallet() {
        featureFlags = InMemoryFeatureFlags(
            setOf(CriOrchestratorFeatureFlag.ENABLED)
        )
        viewModel = SignOutViewModel(navigator, signOutUseCase, featureFlags)
        composeTestRule.setContent {
            SignOutScreen(viewModel, analyticsViewModel, loadingAnalyticsVM)
        }
        composeTestRule.onNode(title).assertIsDisplayed()
        verify(analytics).logEventV3Dot1(
            SignOutAnalyticsViewModel.makeSignOutNoWalletViewEvent(
                context
            )
        )
    }

    @Test
    fun verifySignOutButtonSucceeds() = runBlocking {
        featureFlags = InMemoryFeatureFlags(
            setOf(WalletFeatureFlag.ENABLED, CriOrchestratorFeatureFlag.ENABLED)
        )
        viewModel = SignOutViewModel(navigator, signOutUseCase, featureFlags)
        composeTestRule.setContent {
            SignOutScreen(viewModel, analyticsViewModel, loadingAnalyticsVM)
        }
        composeTestRule.onNode(button).performClick()

        composeTestRule.onNodeWithTag(LOADING_SCREEN_PROGRESS_INDICATOR).assertIsDisplayed()
        verify(analytics).logEventV3Dot1(SignOutAnalyticsViewModel.onPrimaryEvent(context))
        verify(signOutUseCase).invoke(any())
        verify(navigator).navigate(LoginRoutes.Root, true)
    }

    @Test
    fun verifySignOutButtonFails() = runTest {
        featureFlags = InMemoryFeatureFlags(
            setOf(WalletFeatureFlag.ENABLED, CriOrchestratorFeatureFlag.ENABLED)
        )
        viewModel = SignOutViewModel(navigator, signOutUseCase, featureFlags)
        composeTestRule.setContent {
            SignOutScreen(viewModel, analyticsViewModel, loadingAnalyticsVM)
        }
        whenever(signOutUseCase.invoke(any()))
            .thenThrow(SignOutError(Exception("something went wrong")))
        composeTestRule.onNode(button).performClick()
        verify(signOutUseCase).invoke(any())
        verify(navigator).navigate(ErrorRoutes.SignOut, true)
    }

    @Test
    fun verifyCloseIconButton() {
        featureFlags = InMemoryFeatureFlags(
            setOf(WalletFeatureFlag.ENABLED, CriOrchestratorFeatureFlag.ENABLED)
        )
        viewModel = SignOutViewModel(navigator, signOutUseCase, featureFlags)
        composeTestRule.setContent {
            SignOutScreen(viewModel, analyticsViewModel, loadingAnalyticsVM)
        }
        composeTestRule.onNode(closeButton).performClick()

        verify(analytics).logEventV3Dot1(SignOutAnalyticsViewModel.onCloseIcon())
        verify(navigator).goBack()
    }

    @Test
    fun verifyBackButton() {
        featureFlags = InMemoryFeatureFlags(
            setOf(WalletFeatureFlag.ENABLED, CriOrchestratorFeatureFlag.ENABLED)
        )
        viewModel = SignOutViewModel(navigator, signOutUseCase, featureFlags)
        composeTestRule.setContent {
            SignOutScreen(viewModel, analyticsViewModel, loadingAnalyticsVM)
        }
        Espresso.pressBack()

        verify(analytics).logEventV3Dot1(SignOutAnalyticsViewModel.onBackPressed())
        verify(navigator).goBack()
    }
}
