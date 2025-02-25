package uk.gov.onelogin.features.home.ui

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
class HomeScreenAnalyticsViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val analyticsLogger: AnalyticsLogger
) : ViewModel() {
    private val screenEvent = makeScreenEvent(context)
    private val cardLinkEvent = makeCardLinkEvent(context)
    private val backButtonEvent = makeBackButtonEvent(context)

    fun trackScreen() {
        analyticsLogger.logEventV3Dot1(screenEvent)
    }

    fun trackLink() {
        analyticsLogger.logEventV3Dot1(cardLinkEvent)
    }

    fun trackBackButton() {
        analyticsLogger.logEventV3Dot1(backButtonEvent)
    }

    companion object {
        internal fun makeScreenEvent(context: Context) = with(context) {
            ViewEvent.Screen(
                name = getEnglishString(R.string.app_home),
                id = getEnglishString(R.string.home_page_id),
                params = requiredParams
            )
        }

        internal fun makeCardLinkEvent(context: Context) = with(context) {
            TrackEvent.Link(
                isExternal = true,
                domain = getEnglishString(R.string.app_oneLoginCardLinkUrl),
                text = getEnglishString(R.string.app_oneLoginCardLink),
                params = requiredParams
            )
        }

        internal fun makeBackButtonEvent(context: Context) = with(context) {
            TrackEvent.Icon(
                text = getEnglishString(R.string.system_backButton),
                params = requiredParams
            )
        }

        private val requiredParams = RequiredParameters(
            taxonomyLevel2 = TaxonomyLevel2.HOME,
            taxonomyLevel3 = TaxonomyLevel3.UNDEFINED
        )
    }
}
