package uk.gov.onelogin.features.error.ui.offline

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
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

class OfflineErrorAnalyticsViewModelTest {
    private lateinit var name: String
    private lateinit var id: String
    private lateinit var reason: String
    private lateinit var button: String
    private lateinit var backButton: String
    private lateinit var requiredParams: RequiredParameters
    private lateinit var analyticsLogger: AnalyticsLogger
    private lateinit var viewModel: OfflineErrorAnalyticsViewModel

    @Before
    fun setup() {
        val context: Context = ApplicationProvider.getApplicationContext()
        name = context.getEnglishString(R.string.app_networkErrorTitle)
        id = context.getEnglishString(R.string.network_error_screen_id)
        reason = context.getEnglishString(R.string.network_error_reason)
        button = context.getEnglishString(R.string.app_closeButton)
        backButton = context.getEnglishString(R.string.system_backButton)
        requiredParams =
            RequiredParameters(
                taxonomyLevel2 = TaxonomyLevel2.APP_SYSTEM,
                taxonomyLevel3 = TaxonomyLevel3.ERROR
            )
        analyticsLogger = mock()
        viewModel =
            OfflineErrorAnalyticsViewModel(
                context,
                analyticsLogger
            )
    }

    @Test
    fun trackScreen() {
        val event =
            ViewEvent.Error(
                name = name,
                id = id,
                endpoint = "",
                reason = reason,
                status = "",
                params = requiredParams
            )

        viewModel.trackScreen()

        verify(analyticsLogger).logEventV3Dot1(event)
    }

    @Test
    fun trackButton() {
        val event =
            TrackEvent.Button(
                text = button,
                params = requiredParams
            )

        viewModel.trackButton()

        verify(analyticsLogger).logEventV3Dot1(event)
    }

    @Test
    fun trackBackButton() {
        val event =
            TrackEvent.Icon(
                text = backButton,
                params = requiredParams
            )

        viewModel.trackBackButton()

        verify(analyticsLogger).logEventV3Dot1(event)
    }
}
