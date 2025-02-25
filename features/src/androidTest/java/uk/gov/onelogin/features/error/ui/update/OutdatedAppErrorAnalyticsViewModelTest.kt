package uk.gov.onelogin.features.error.ui.update

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
import uk.gov.onelogin.features.appinfo.AppInfoUtils

class OutdatedAppErrorAnalyticsViewModelTest {
    private lateinit var domain: String
    private lateinit var buttonText: String
    private lateinit var name: String
    private lateinit var id: String
    private lateinit var iconText: String
    private lateinit var logger: AnalyticsLogger
    private lateinit var requiredParameters: RequiredParameters
    private lateinit var viewModel: OutdatedAppErrorAnalyticsViewModel

    @Before
    fun setUp() {
        val context: Context = ApplicationProvider.getApplicationContext()
        logger = mock()
        requiredParameters =
            RequiredParameters(
                taxonomyLevel2 = TaxonomyLevel2.APP_SYSTEM,
                taxonomyLevel3 = TaxonomyLevel3.UNDEFINED
            )
        domain = AppInfoUtils.GOOGLE_PLAY_URL.domain
        buttonText = context.getEnglishString(R.string.app_updateAppButton)
        name = context.getEnglishString(R.string.app_updateApp_Title)
        id = context.getEnglishString(R.string.update_required_page_id)
        iconText = context.getEnglishString(R.string.system_backButton)
        viewModel = OutdatedAppErrorAnalyticsViewModel(context, logger)
    }

    @Test
    fun trackAppUpdateLogsTrackEventLink() {
        // Given a TrackEvent.Link
        val event =
            TrackEvent.Link(
                isExternal = true,
                domain = domain,
                text = buttonText,
                params = requiredParameters
            )
        // When tracking an app update
        viewModel.trackAppUpdate()
        // Then log a TrackEvent to the AnalyticsLogger
        verify(logger).logEventV3Dot1(event)
    }

    @Test
    fun trackSignOutViewLogsScreenView() {
        // Given a ViewEvent.Screen
        val event =
            ViewEvent.Screen(
                name = name,
                id = id,
                params = requiredParameters
            )
        // When tracking the update required screen view
        viewModel.trackUpdateRequiredView()
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
