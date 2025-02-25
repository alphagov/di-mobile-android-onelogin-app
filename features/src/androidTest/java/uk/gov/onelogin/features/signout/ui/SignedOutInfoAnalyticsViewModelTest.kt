package uk.gov.onelogin.features.signout.ui

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.verify
import uk.gov.android.onelogin.core.R
import uk.gov.logging.api.analytics.extensions.domain
import uk.gov.logging.api.analytics.extensions.getEnglishString
import uk.gov.logging.api.analytics.logging.AnalyticsLogger
import uk.gov.logging.api.analytics.parameters.data.TaxonomyLevel2
import uk.gov.logging.api.analytics.parameters.data.TaxonomyLevel3
import uk.gov.logging.api.v3dot1.logger.logEventV3Dot1
import uk.gov.logging.api.v3dot1.model.RequiredParameters
import uk.gov.logging.api.v3dot1.model.TrackEvent
import uk.gov.logging.api.v3dot1.model.ViewEvent

class SignedOutInfoAnalyticsViewModelTest {
    private lateinit var domain: String
    private lateinit var buttonText: String
    private lateinit var name: String
    private lateinit var id: String
    private lateinit var logger: AnalyticsLogger
    private lateinit var requiredParameters: RequiredParameters
    private lateinit var viewModel: SignedOutInfoAnalyticsViewModel

    @Before
    fun setUp() {
        val context: Context = ApplicationProvider.getApplicationContext()
        logger = mock()
        requiredParameters =
            RequiredParameters(
                taxonomyLevel2 = TaxonomyLevel2.LOGIN,
                taxonomyLevel3 = TaxonomyLevel3.RE_AUTH
            )
        domain = context.getEnglishString(R.string.baseStsUrl, "").domain
        buttonText = context.getEnglishString(R.string.app_SignInWithGovUKOneLoginButton)
        name = context.getEnglishString(R.string.app_youveBeenSignedOutTitle)
        id = context.getEnglishString(R.string.signed_out_info_page_id)
        viewModel = SignedOutInfoAnalyticsViewModel(context, logger)
    }

    @Test
    fun trackSignOutLogsTrackEventLink() {
        // Given a TrackEvent.Link
        val event =
            TrackEvent.Link(
                isExternal = false,
                domain = domain,
                text = buttonText,
                params = requiredParameters
            )
        // When tracking re-auth
        viewModel.trackReAuth()
        // Then log a TrackEvent to the AnalyticsLogger
        verify(logger).logEventV3Dot1(event)
    }

    @Test
    fun trackSignOutViewLogsViewEventScreen() {
        // Given a ViewEvent.Screen
        val event =
            ViewEvent.Screen(
                name = name,
                id = id,
                params = requiredParameters
            )
        // When tracking the signed out info screen view
        viewModel.trackSignOutInfoView()
        // Then log a ScreenView to the AnalyticsLogger
        verify(logger).logEventV3Dot1(event)
    }
}
