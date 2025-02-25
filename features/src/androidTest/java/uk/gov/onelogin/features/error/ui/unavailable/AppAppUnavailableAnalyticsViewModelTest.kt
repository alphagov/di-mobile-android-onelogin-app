package uk.gov.onelogin.features.error.ui.unavailable

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

class AppAppUnavailableAnalyticsViewModelTest {
    private lateinit var name: String
    private lateinit var id: String
    private lateinit var iconText: String
    private lateinit var logger: AnalyticsLogger
    private lateinit var requiredParameters: RequiredParameters
    private lateinit var viewModel: AppUnavailableAnalyticsViewModel

    @Before
    fun setUp() {
        val context: Context = ApplicationProvider.getApplicationContext()
        logger = mock()
        requiredParameters =
            RequiredParameters(
                taxonomyLevel2 = TaxonomyLevel2.APP_SYSTEM,
                taxonomyLevel3 = TaxonomyLevel3.UNDEFINED
            )
        name = context.getEnglishString(R.string.app_appUnavailableTitle)
        id = context.getEnglishString(R.string.app_unavailable_page_id)
        iconText = context.getEnglishString(R.string.system_backButton)
        viewModel = AppUnavailableAnalyticsViewModel(context, logger)
    }

    @Test
    fun trackAppUnavailableViewLogsScreenView() {
        // Given a ViewEvent.Screen
        val event =
            ViewEvent.Screen(
                name = name,
                id = id,
                params = requiredParameters
            )
        // When tracking the app unavailable screen view
        viewModel.trackUnavailableView()
        // Then log a ScreenView to the AnalyticsLogger
        verify(logger).logEventV3Dot1(event)
    }

    @Test
    fun trackBackButtonLogsTrackEventIcon() {
        // Given a TrackEvent.Icon
        val event =
            TrackEvent.Icon(
                text = iconText,
                params = requiredParameters
            )
        // When tracking the hardware back button
        viewModel.trackBackButton()
        // Then log a TrackEvent to the AnalyticsLogger
        verify(logger).logEventV3Dot1(event)
    }
}
