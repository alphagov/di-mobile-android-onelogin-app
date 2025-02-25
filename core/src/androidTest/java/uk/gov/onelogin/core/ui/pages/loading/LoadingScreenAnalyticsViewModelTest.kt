package uk.gov.onelogin.core.ui.pages.loading

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

class LoadingScreenAnalyticsViewModelTest {
    private lateinit var name: String
    private lateinit var id: String
    private lateinit var nativeBackBtn: String
    private lateinit var logger: AnalyticsLogger
    private lateinit var requiredParameters: RequiredParameters
    private lateinit var viewModel: LoadingScreenAnalyticsViewModel

    @Before
    fun setup() {
        val context: Context = ApplicationProvider.getApplicationContext()
        logger = mock()
        requiredParameters =
            RequiredParameters(
                taxonomyLevel2 = TaxonomyLevel2.APP_SYSTEM,
                taxonomyLevel3 = TaxonomyLevel3.UNDEFINED
            )
        name = context.getString(R.string.app_loadingBody)
        id = context.getString(R.string.app_loading_page_id)
        nativeBackBtn = context.getEnglishString(R.string.system_backButton)
        viewModel = LoadingScreenAnalyticsViewModel(context, logger)
    }

    @Test
    fun trackLoadingScreenViewEvent() {
        val event =
            ViewEvent.Screen(
                name = name,
                id = id,
                params = requiredParameters
            )

        viewModel.trackLoadingScreenEvent()

        verify(logger).logEventV3Dot1(event)
    }

    @Test
    fun trackBackEvent() {
        val event =
            TrackEvent.Icon(
                text = nativeBackBtn,
                params = requiredParameters
            )

        viewModel.trackBackButton()

        verify(logger).logEventV3Dot1(event)
    }
}
