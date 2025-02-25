package uk.gov.onelogin.features.error.ui.generic

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
class GenericErrorAnalyticsViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val analyticsLogger: AnalyticsLogger
) : ViewModel() {
    private val screenEvent = makeScreenEvent(context)
    private val buttonEvent = makeButtonEvent(context)
    private val backButtonEvent = makeBackEvent(context)

    fun trackScreen() {
        analyticsLogger.logEventV3Dot1(screenEvent)
    }

    fun trackButton() {
        analyticsLogger.logEventV3Dot1(buttonEvent)
    }

    fun trackBackButton() {
        analyticsLogger.logEventV3Dot1(backButtonEvent)
    }

    companion object {
        internal fun makeScreenEvent(context: Context) = with(context) {
            ViewEvent.Error(
                name = getEnglishString(R.string.app_somethingWentWrongErrorTitle),
                id = getEnglishString(R.string.generic_error_screen_id),
                endpoint = "",
                reason = getEnglishString(R.string.generic_error_reason),
                status = "",
                params = requiredParams
            )
        }

        internal fun makeButtonEvent(context: Context) = with(context) {
            TrackEvent.Button(
                text = getEnglishString(R.string.app_closeButton),
                params = requiredParams
            )
        }

        internal fun makeBackEvent(context: Context) = with(context) {
            TrackEvent.Icon(
                text = getEnglishString(R.string.system_backButton),
                params = requiredParams
            )
        }

        private val requiredParams = RequiredParameters(
            taxonomyLevel2 = TaxonomyLevel2.APP_SYSTEM,
            taxonomyLevel3 = TaxonomyLevel3.ERROR
        )
    }
}
