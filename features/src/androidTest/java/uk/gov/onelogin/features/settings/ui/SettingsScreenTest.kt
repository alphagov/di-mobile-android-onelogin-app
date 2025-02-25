package uk.gov.onelogin.features.settings.ui

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.net.Uri
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.click
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTouchInput
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.matcher.IntentMatchers.hasData
import androidx.test.espresso.intent.matcher.IntentMatchers.isInternal
import androidx.test.espresso.intent.matcher.UriMatchers
import kotlinx.coroutines.flow.MutableStateFlow
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.not
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import uk.gov.android.onelogin.core.R
import uk.gov.logging.api.analytics.logging.AnalyticsLogger
import uk.gov.logging.api.v3dot1.logger.logEventV3Dot1
import uk.gov.onelogin.core.navigation.data.SignOutRoutes
import uk.gov.onelogin.core.navigation.domain.Navigator
import uk.gov.onelogin.core.tokens.domain.retrieve.GetEmail
import uk.gov.onelogin.features.TestCase
import uk.gov.onelogin.features.optin.data.OptInRepository
import uk.gov.onelogin.features.optin.ui.NOTICE_TAG

class SettingsScreenTest : TestCase() {
    private val optInRepository: OptInRepository = mock()
    private lateinit var navigator: Navigator
    private lateinit var getEmail: GetEmail
    private lateinit var viewModel: SettingsScreenViewModel
    private lateinit var analytics: AnalyticsLogger
    private lateinit var analyticsViewModel: SettingsAnalyticsViewModel

    private lateinit var yourDetailsHeader: SemanticsMatcher
    private lateinit var yourDetailsTitle: SemanticsMatcher
    private lateinit var yourDetailsSubTitle: SemanticsMatcher
    private lateinit var legalLink1: SemanticsMatcher
    private lateinit var legalLink2: SemanticsMatcher
    private lateinit var legalLink3: SemanticsMatcher
    private lateinit var helpLink: SemanticsMatcher
    private lateinit var contactLink: SemanticsMatcher
    private lateinit var aboutTheAppSwitch: SemanticsMatcher
    private lateinit var aboutTheAppSubTitle: SemanticsMatcher
    private lateinit var aboutTheAppPrivacyLink: SemanticsMatcher
    private lateinit var signOutButton: SemanticsMatcher
    private lateinit var openSourceLicensesButton: SemanticsMatcher

    @Before
    fun setUp() {
        whenever(optInRepository.isOptInPreferenceRequired())
            .thenReturn(MutableStateFlow(false))
        whenever(optInRepository.hasAnalyticsOptIn()).thenReturn(MutableStateFlow(false))
        navigator = mock()
        getEmail = mock()
        viewModel = SettingsScreenViewModel(optInRepository, navigator, getEmail)
        analytics = mock()
        analyticsViewModel = SettingsAnalyticsViewModel(context, analytics)
        yourDetailsHeader = hasText(resources.getString(R.string.app_settingsSubtitle1))
        yourDetailsTitle = hasText(resources.getString(R.string.app_settingsSignInDetailsLink))
        yourDetailsSubTitle = hasText(
            resources.getString(R.string.app_settingSignInDetailsFootnote)
        )
        legalLink1 = hasText(resources.getString(R.string.app_privacyNoticeLink2))
        legalLink2 = hasText(resources.getString(R.string.app_openSourceLicences))
        legalLink3 = hasText(resources.getString(R.string.app_accessibilityStatement))
        helpLink = hasText(resources.getString(R.string.app_appGuidanceLink))
        contactLink = hasText(resources.getString(R.string.app_contactLink))
        aboutTheAppSwitch = hasTestTag(resources.getString(R.string.optInSwitchTestTag))
        aboutTheAppSubTitle = hasText(
            resources.getString(R.string.app_settingsAnalyticsToggleFootnote),
            substring = true
        )
        aboutTheAppPrivacyLink = hasTestTag(NOTICE_TAG)
        signOutButton = hasText(resources.getString(R.string.app_signOutButton))
        openSourceLicensesButton = hasText(resources.getString(R.string.app_openSourceLicences))
        Intents.init()
        intending(not(isInternal())).respondWith(
            Instrumentation.ActivityResult(Activity.RESULT_OK, null)
        )
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    @Test
    fun yourDetailsGroupDisplayed() {
        composeTestRule.setContent {
            SettingsScreen(viewModel, analyticsViewModel)
        }
        composeTestRule.onNode(yourDetailsHeader).assertIsDisplayed()
        composeTestRule.onNode(yourDetailsTitle).assertIsDisplayed()
        composeTestRule.onNode(yourDetailsSubTitle).assertIsDisplayed()
        verify(analytics).logEventV3Dot1(SettingsAnalyticsViewModel.makeSettingsViewEvent(context))
    }

    @Test
    fun legalGroupDisplayed() {
        composeTestRule.setContent {
            SettingsScreen(viewModel, analyticsViewModel)
        }
        composeTestRule.onNode(legalLink1).performScrollTo().assertIsDisplayed()
        composeTestRule.onNode(legalLink2).performScrollTo().assertIsDisplayed()
        composeTestRule.onNode(legalLink3).performScrollTo().assertIsDisplayed()
    }

    @Test
    fun aboutTheAppSectionDisplayed() {
        composeTestRule.setContent {
            SettingsScreen(viewModel, analyticsViewModel)
        }
        composeTestRule.onNode(aboutTheAppSwitch).performScrollTo().assertIsDisplayed()
        composeTestRule.onNode(aboutTheAppPrivacyLink).performScrollTo().assertIsDisplayed()
        composeTestRule.onNode(aboutTheAppSubTitle).assertIsDisplayed()
    }

    @Test
    fun toggleSwitchCallOnToggleClickEvent() {
        var optInState = false
        composeTestRule.setContent {
            PreferenceToggleRow(
                title = R.string.app_settingsAnalyticsToggle,
                checked = optInState,
                onToggle = { optInState = true }
            )
        }
        composeTestRule.onNode(aboutTheAppSwitch).performClick()
        assert(optInState)
    }

    @Test
    fun privacyNoticeInAboutTheAppSectionLaunchesBrowser() {
        var optInState = false
        var privacyNoticeClicked = false
        composeTestRule.setContent {
            AboutTheAppSection(
                optInState,
                onToggle = { optInState = true },
                onPrivacyNoticeClick = { privacyNoticeClicked = true }
            )
        }
        composeTestRule.onNode(aboutTheAppPrivacyLink, useUnmergedTree = true)
            .performTouchInput {
                click(bottomRight.copy(x = bottomRight.x - 10f))
            }
        assertTrue(privacyNoticeClicked)
    }

    @Test
    fun signOutCta() {
        composeTestRule.setContent {
            SettingsScreen(viewModel, analyticsViewModel)
        }
        composeTestRule.onNode(signOutButton).performScrollTo().performClick()
        verify(navigator).navigate(SignOutRoutes.Start)
        verify(analytics).logEventV3Dot1(SettingsAnalyticsViewModel.makeSignOutEvent(context))
    }

    @Test
    fun openSourceLicensesCta() {
        composeTestRule.setContent {
            SettingsScreen(viewModel, analyticsViewModel)
        }
        composeTestRule.onNode(openSourceLicensesButton).performScrollTo().performClick()
        verify(analytics).logEventV3Dot1(SettingsAnalyticsViewModel.makeOpenSourceEvent(context))
    }

    @Test
    fun signInLaunchesBrowser() {
        // Given the SettingsScreen Composable
        val url = resources.getString(R.string.app_manageSignInDetailsUrl)
        checkTheLinkOpensTheCorrectUrl(yourDetailsTitle, url)
        verify(analytics).logEventV3Dot1(SettingsAnalyticsViewModel.makeSignInDetailsEvent(context))
    }

    @Test
    fun helpLaunchesBrowser() {
        // Given the SettingsScreen Composable
        val url = resources.getString(R.string.app_helpUrl)
        checkTheLinkOpensTheCorrectUrl(helpLink, url)
        verify(analytics).logEventV3Dot1(SettingsAnalyticsViewModel.makeUsingOneLoginEvent(context))
    }

    @Test
    fun contactLaunchesBrowser() {
        // Given the SettingsScreen Composable
        val url = resources.getString(R.string.app_contactUrl)
        checkTheLinkOpensTheCorrectUrl(contactLink, url)
        verify(analytics).logEventV3Dot1(SettingsAnalyticsViewModel.makeContactEvent(context))
    }

    @Test
    fun privacyNoticeLaunchesBrowser() {
        // Given the SettingsScreen Composable
        val url = resources.getString(R.string.privacy_notice_url)
        checkTheLinkOpensTheCorrectUrl(legalLink1, url)
        verify(analytics).logEventV3Dot1(SettingsAnalyticsViewModel.makePrivacyNoticeEvent(context))
    }

    @Test
    fun accessibilityStatementLaunchesBrowser() {
        // Given the SettingsScreen Composable
        val url = resources.getString(R.string.app_accessibilityStatementUrl)
        checkTheLinkOpensTheCorrectUrl(legalLink3, url)
        verify(analytics).logEventV3Dot1(SettingsAnalyticsViewModel.makeAccessibilityEvent(context))
    }

    private fun checkTheLinkOpensTheCorrectUrl(linkView: SemanticsMatcher, url: String) {
        val signInURL = Uri.parse(url)
        composeTestRule.setContent {
            SettingsScreen(viewModel, analyticsViewModel)
        }
        // When clicking on the link

        composeTestRule.onNode(linkView).performScrollTo().performClick()
        // Then open a browser with the correct URL
        val host = signInURL.host
        val path = signInURL.path
        val scheme = signInURL.scheme
        intended(
            allOf(
                IntentMatchers.hasAction(Intent.ACTION_VIEW),
                hasData(UriMatchers.hasHost(host)),
                hasData(UriMatchers.hasPath(path)),
                hasData(UriMatchers.hasScheme(scheme))
            )
        )
    }
}
