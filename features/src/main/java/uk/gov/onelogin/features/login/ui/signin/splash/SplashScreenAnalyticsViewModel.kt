package uk.gov.onelogin.features.login.ui.signin.splash

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
class SplashScreenAnalyticsViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val analyticsLogger: AnalyticsLogger
) : ViewModel() {
    private val unlockBtnEvent = makeButtonEvent(context)

    fun trackSplashScreen(
        context: Context,
        isLocked: Boolean
    ) {
        val event = makeScreenEvent(context, isLocked)
        analyticsLogger.logEventV3Dot1(event)
    }

    fun trackUnlockButton() {
        analyticsLogger.logEventV3Dot1(unlockBtnEvent)
    }

    fun trackBackButton(context: Context, isLocked: Boolean) {
        analyticsLogger.logEventV3Dot1(makeBackEvent(context, isLocked))
    }

    companion object {
        internal fun makeScreenEvent(context: Context, isLocked: Boolean) = with(context) {
            val correctDetails = getCorrectDetails(isLocked)

            ViewEvent.Screen(
                name = getEnglishString(correctDetails.first),
                id = getEnglishString(correctDetails.second),
                params = RequiredParameters(
                    taxonomyLevel2 = TaxonomyLevel2.LOGIN,
                    taxonomyLevel3 = correctDetails.third
                )
            )
        }

        internal fun makeButtonEvent(context: Context) = with(context) {
            TrackEvent.Button(
                text = getEnglishString(R.string.app_unlockButton),
                params = RequiredParameters(
                    taxonomyLevel2 = TaxonomyLevel2.LOGIN,
                    taxonomyLevel3 = TaxonomyLevel3.UNLOCK
                )
            )
        }

        internal fun makeBackEvent(context: Context, isLocked: Boolean) = with(context) {
            TrackEvent.Icon(
                text = getEnglishString(R.string.system_backButton),
                params = RequiredParameters(
                    taxonomyLevel2 = TaxonomyLevel2.LOGIN,
                    taxonomyLevel3 = if (isLocked) {
                        TaxonomyLevel3.UNLOCK
                    } else {
                        TaxonomyLevel3.UNDEFINED
                    }
                )
            )
        }

        private fun getCorrectDetails(isLocked: Boolean): Triple<Int, Int, TaxonomyLevel3> {
            return if (isLocked) {
                Triple(
                    R.string.app_splashScreenUnlockAnalyticsScreenName,
                    R.string.splash_unlock_screen_page_id,
                    TaxonomyLevel3.UNLOCK
                )
            } else {
                Triple(
                    R.string.app_splashScreenAnalyticsScreenName,
                    R.string.splash_screen_page_id,
                    TaxonomyLevel3.UNDEFINED
                )
            }
        }
    }
}
