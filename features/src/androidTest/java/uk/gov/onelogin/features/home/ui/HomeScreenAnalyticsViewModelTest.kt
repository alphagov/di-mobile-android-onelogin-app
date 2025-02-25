package uk.gov.onelogin.features.home.ui

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.verify
import uk.gov.android.onelogin.core.R
import uk.gov.logging.api.analytics.extensions.getEnglishString
import uk.gov.logging.api.analytics.logging.AnalyticsLogger
import uk.gov.logging.api.analytics.parameters.data.TaxonomyLevel2
import uk.gov.logging.api.analytics.parameters.data.TaxonomyLevel3
import uk.gov.logging.api.v3dot1.logger.logEventV3Dot1
import uk.gov.logging.api.v3dot1.model.RequiredParameters
import uk.gov.logging.api.v3dot1.model.TrackEvent
import uk.gov.logging.api.v3dot1.model.ViewEvent

class HomeScreenAnalyticsViewModelTest {
    private lateinit var domain: String
    private lateinit var linkText: String
    private lateinit var name: String
    private lateinit var id: String
    private lateinit var back: String
    private lateinit var requiredParameters: RequiredParameters
    private lateinit var logger: AnalyticsLogger
    private lateinit var viewModel: HomeScreenAnalyticsViewModel

    @Before
    fun setUp() {
        val context: Context = ApplicationProvider.getApplicationContext()
        requiredParameters =
            RequiredParameters(
                taxonomyLevel2 = TaxonomyLevel2.HOME,
                taxonomyLevel3 = TaxonomyLevel3.UNDEFINED
            )
        logger = mock()
        domain = context.getEnglishString(R.string.app_oneLoginCardLinkUrl)
        linkText = context.getEnglishString(R.string.app_oneLoginCardLink)
        name = context.getEnglishString(R.string.app_home)
        id = context.getEnglishString(R.string.home_page_id)
        back = context.getEnglishString(R.string.system_backButton)
        viewModel = HomeScreenAnalyticsViewModel(context, logger)
    }

    @Test
    fun trackScreen() {
        // Given a ViewEvent.Screen
        val event =
            ViewEvent.Screen(
                name = name,
                id = id,
                params = requiredParameters
            )
        // When tracking the home screen
        viewModel.trackScreen()
        // Then log a ScreenView to the AnalyticsLogger
        verify(logger).logEventV3Dot1(event)
    }

    @Test
    fun trackEventLink() {
        // Given a TrackEvent.Link
        val event =
            TrackEvent.Link(
                isExternal = true,
                domain = domain,
                text = linkText,
                params = requiredParameters
            )
        // When tracking redirect to "Using your ..." web page
        viewModel.trackLink()
        // Then log a TrackEvent to the AnalyticsLogger
        verify(logger).logEventV3Dot1(event)
    }

    @Test
    fun trackBackButton() {
        // Given a TrackEvent.Icon
        val event =
            TrackEvent.Icon(
                text = back,
                params = requiredParameters
            )
        // When tracking the hardware back button
        viewModel.trackBackButton()
        // Then log a TrackEvent to the AnalyticsLogger
        verify(logger).logEventV3Dot1(event)
    }
}
