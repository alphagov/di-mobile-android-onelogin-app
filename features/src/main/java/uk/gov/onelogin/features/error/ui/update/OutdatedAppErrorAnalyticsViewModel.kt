package uk.gov.onelogin.features.error.ui.update

import android.content.Context
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
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

@HiltViewModel
class OutdatedAppErrorAnalyticsViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val analyticsLogger: AnalyticsLogger
) : ViewModel() {
    private val updateEvent = makeUpdateEvent(context)
    private val updateRequiredViewEvent = makeUpdateRequiredViewEvent(context)
    private val backEvent = makeBackEvent(context)

    fun trackAppUpdate() {
        analyticsLogger.logEventV3Dot1(updateEvent)
    }

    fun trackUpdateRequiredView() {
        analyticsLogger.logEventV3Dot1(updateRequiredViewEvent)
    }

    fun trackBackButton() {
        analyticsLogger.logEventV3Dot1(backEvent)
    }

    companion object {
        fun makeUpdateEvent(context: Context) = with(context) {
            TrackEvent.Link(
                isExternal = true,
                domain = AppInfoUtils.GOOGLE_PLAY_URL.domain,
                text = getEnglishString(R.string.app_updateAppButton),
                params =
                RequiredParameters(
                    taxonomyLevel2 = TaxonomyLevel2.APP_SYSTEM,
                    taxonomyLevel3 = TaxonomyLevel3.UNDEFINED
                )
            )
        }

        fun makeUpdateRequiredViewEvent(context: Context) = with(context) {
            ViewEvent.Screen(
                name = getEnglishString(R.string.app_updateApp_Title),
                id = getEnglishString(R.string.update_required_page_id),
                params =
                RequiredParameters(
                    taxonomyLevel2 = TaxonomyLevel2.APP_SYSTEM,
                    taxonomyLevel3 = TaxonomyLevel3.UNDEFINED
                )
            )
        }

        fun makeBackEvent(context: Context) = with(context) {
            TrackEvent.Icon(
                text = getEnglishString(R.string.system_backButton),
                params =
                RequiredParameters(
                    taxonomyLevel2 = TaxonomyLevel2.APP_SYSTEM,
                    taxonomyLevel3 = TaxonomyLevel3.UNDEFINED
                )
            )
        }
    }
}
