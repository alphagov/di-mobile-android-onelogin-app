package uk.gov.onelogin.features.home.ui

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.espresso.Espresso
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.hasData
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import uk.gov.android.featureflags.FeatureFlags
import uk.gov.android.featureflags.InMemoryFeatureFlags
import uk.gov.android.network.client.GenericHttpClient
import uk.gov.android.onelogin.core.R
import uk.gov.logging.api.analytics.logging.AnalyticsLogger
import uk.gov.onelogin.core.navigation.domain.Navigator
import uk.gov.onelogin.features.TestCase
import uk.gov.onelogin.features.ext.setupComposeTestRule
import uk.gov.onelogin.features.featureflags.data.CriOrchestratorFeatureFlag
import uk.gov.onelogin.features.featureflags.data.WalletFeatureFlag

@Suppress("ForbiddenComment")
class HomeScreenKtTest : TestCase() {
    private lateinit var httpClient: GenericHttpClient
    private lateinit var analyticsLogger: AnalyticsLogger

    // TODO: Remove this after `activeSession` has been added to the CriOrchestrator and test using the stub
    //  provided
    private lateinit var featureFlags: FeatureFlags
    private lateinit var navigator: Navigator
    private lateinit var viewModel: HomeScreenViewModel
    private lateinit var analytics: AnalyticsLogger
    private lateinit var analyticsViewModel: HomeScreenAnalyticsViewModel

    private val intent = Intent()
    private val result = Instrumentation.ActivityResult(Activity.RESULT_OK, intent)

    @Before
    fun setup() {
        Intents.init()
        httpClient = mock()
        analyticsLogger = mock()
        featureFlags =
            InMemoryFeatureFlags(
                setOf(WalletFeatureFlag.ENABLED, CriOrchestratorFeatureFlag.ENABLED)
            )
        navigator = mock()
        viewModel = HomeScreenViewModel(httpClient, analyticsLogger, featureFlags, navigator)
        analytics = mock()
        analyticsViewModel = HomeScreenAnalyticsViewModel(context, analytics)
        composeTestRule.setupComposeTestRule { _ ->
            HomeScreen(viewModel, analyticsViewModel)
        }
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    @Test
    fun homeScreenDisplayed() {
        composeTestRule.apply {
            waitUntil(TIMEOUT) {
                onNodeWithContentDescription(
                    "Close",
                    useUnmergedTree = true
                ).isDisplayed()
            }

            onNodeWithContentDescription(
                "Close",
                useUnmergedTree = true
            ).performClick()

            onNodeWithText(
                resources.getString(R.string.app_homeTitle)
            ).assertIsDisplayed()

            onNodeWithTag(
                resources.getString(R.string.appCriCardTestTag),
                useUnmergedTree = true
            ).assertIsDisplayed()

            onNodeWithText(
                "Continue proving your identity",
                useUnmergedTree = true
            ).performClick()

            waitUntil(TIMEOUT) {
                onNodeWithContentDescription(
                    "Close",
                    useUnmergedTree = true
                ).isDisplayed()
            }

            onNodeWithContentDescription(
                "Close",
                useUnmergedTree = true
            ).performClick()

            onNodeWithTag(
                resources.getString(R.string.yourServicesCardTestTag),
                useUnmergedTree = true
            ).assertIsDisplayed()

            onNodeWithText("Developer Panel").assertIsDisplayed()
        }
    }

    @Test
    fun analyticsTriggered() {
        // This will stop the test from throwing an error due to missing intent on pipeline
        intending(hasData(context.getString(R.string.app_oneLoginCardLinkUrl))).respondWith(result)
        composeTestRule.apply {
            waitUntil(TIMEOUT) {
                onNodeWithContentDescription(
                    "Close",
                    useUnmergedTree = true
                ).isDisplayed()
            }

            onNodeWithContentDescription(
                "Close",
                useUnmergedTree = true
            ).performClick()

            Espresso.pressBack()

            onNodeWithText(
                resources.getString(R.string.app_oneLoginCardLink),
                useUnmergedTree = true,
                substring = true
            ).performClick()
        }
    }

    companion object {
        private const val TIMEOUT = 10000L
    }
}
