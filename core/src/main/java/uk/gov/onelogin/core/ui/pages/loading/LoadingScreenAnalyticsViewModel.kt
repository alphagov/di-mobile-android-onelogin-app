package uk.gov.onelogin.core.ui.pages.loading

import android.content.Context
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import uk.gov.android.onelogin.core.R
import uk.gov.logging.api.analytics.extensions.getEnglishString
import uk.gov.logging.api.analytics.logging.AnalyticsLogger
import uk.gov.logging.api.analytics.parameters.data.TaxonomyLevel2
import uk.gov.logging.api.analytics.parameters.data.TaxonomyLevel3
import uk.gov.logging.api.v3dot1.logger.logEventV3Dot1
import uk.gov.logging.api.v3dot1.model.RequiredParameters
import uk.gov.logging.api.v3dot1.model.TrackEvent
import uk.gov.logging.api.v3dot1.model.ViewEvent

@HiltViewModel
class LoadingScreenAnalyticsViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val analyticsLogger: AnalyticsLogger
) : ViewModel() {
    private val updateLoadingViewEvent = makeLoadingScreenViewEvent(context)
    private val backEvent = makeBackEvent(context)

    fun trackLoadingScreenEvent() {
        analyticsLogger.logEventV3Dot1(updateLoadingViewEvent)
    }

    fun trackBackButton() {
        analyticsLogger.logEventV3Dot1(backEvent)
    }

    private fun makeLoadingScreenViewEvent(context: Context) =
        with(context) {
            ViewEvent.Screen(
                name = getEnglishString(R.string.app_loadingBody),
                id = getEnglishString(R.string.app_loading_page_id),
                params = RequiredParameters(
                    taxonomyLevel2 = TaxonomyLevel2.APP_SYSTEM,
                    taxonomyLevel3 = TaxonomyLevel3.UNDEFINED
                )
            )
        }

    private fun makeBackEvent(context: Context) =
        with(context) {
            TrackEvent.Icon(
                text = getEnglishString(R.string.system_backButton),
                params = RequiredParameters(
                    taxonomyLevel2 = TaxonomyLevel2.APP_SYSTEM,
                    taxonomyLevel3 = TaxonomyLevel3.UNDEFINED
                )
            )
        }
}
