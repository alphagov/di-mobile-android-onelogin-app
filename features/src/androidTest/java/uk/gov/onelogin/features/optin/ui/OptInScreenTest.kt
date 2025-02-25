package uk.gov.onelogin.features.optin.ui

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.net.Uri
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.performClick
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.matcher.IntentMatchers.hasData
import androidx.test.espresso.intent.matcher.IntentMatchers.isInternal
import androidx.test.espresso.intent.matcher.UriMatchers
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.not
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyBlocking
import org.mockito.kotlin.wheneverBlocking
import uk.gov.android.onelogin.core.R
import uk.gov.onelogin.core.navigation.data.LoginRoutes
import uk.gov.onelogin.core.navigation.domain.Navigator
import uk.gov.onelogin.features.TestCase
import uk.gov.onelogin.features.optin.data.OptInRepository

class OptInScreenTest : TestCase() {
    private lateinit var privacyNotice: SemanticsMatcher
    private lateinit var primaryButton: SemanticsMatcher
    private lateinit var textButton: SemanticsMatcher
    private val repository: OptInRepository = mock()
    private val mockNavigator: Navigator = mock()
    private lateinit var viewModel: OptInViewModel

    @Before
    fun setUp() {
        privacyNotice = hasTestTag(NOTICE_TAG)
        primaryButton = hasText(resources.getString(R.string.app_shareAnalyticsButton))
        textButton = hasText(resources.getString(R.string.app_doNotShareAnalytics))
        wheneverBlocking { repository.optIn() }.thenAnswer { }
        wheneverBlocking { repository.optOut() }.thenAnswer { }
        viewModel =
            OptInViewModel(
                repository,
                mockNavigator
            )
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
    fun shareAnalytics() {
        // Given the OptInScreen Composable
        composeTestRule.setContent {
            OptInScreen(viewModel)
        }
        // When clicking on the "Share analytics" `primaryButton`
        composeTestRule.onNode(primaryButton).performClick()
        // Then call optIn()
        verify(mockNavigator).navigate(LoginRoutes.Welcome, true)
        verifyBlocking(repository) { optIn() }
    }

    @Test
    fun doNotShareAnalytics() {
        // Given the OptOutScreen Composable
        composeTestRule.setContent {
            OptInScreen(viewModel)
        }
        // When clicking on the "Do not share analytics" `textButton`
        composeTestRule.onNode(textButton).performClick()
        // Then call optOut()
        verify(mockNavigator).navigate(LoginRoutes.Welcome, true)
        verifyBlocking(repository) { optOut() }
    }

    @Test
    fun privacyNoticeLaunchesBrowser() {
        // Given the OptOutScreen Composable
        val url = resources.getString(R.string.privacy_notice_url)
        val privacyURL = Uri.parse(url)
        composeTestRule.setContent {
            OptInScreen(viewModel)
        }
        // When clicking on the privacy notice link
        composeTestRule.onNode(privacyNotice).performClick()
        // Then open a browser with the correct URL
        val host = privacyURL.host
        val path = privacyURL.path
        val scheme = privacyURL.scheme
        intended(
            allOf(
                IntentMatchers.hasAction(Intent.ACTION_VIEW),
                hasData(UriMatchers.hasHost(host)),
                hasData(UriMatchers.hasPath(path)),
                hasData(UriMatchers.hasScheme(scheme))
            )
        )
    }

    @Test
    fun previewTest() {
        composeTestRule.setContent {
            OptInPreview()
        }
    }
}
