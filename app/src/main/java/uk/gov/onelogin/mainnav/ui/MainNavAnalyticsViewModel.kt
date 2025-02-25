package uk.gov.onelogin.mainnav.ui

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

@HiltViewModel
class MainNavAnalyticsViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val analyticsLogger: AnalyticsLogger
) : ViewModel() {
    private val homeButtonEvent = makeHomeButtonEvent(context)
    private val walletButtonEvent = makeWalletButtonEvent(context)
    private val settingsButtonEvent = makeSettingsButtonEvent(context)

    fun trackHomeTabButton() {
        analyticsLogger.logEventV3Dot1(homeButtonEvent)
    }

    fun trackWalletTabButton() {
        analyticsLogger.logEventV3Dot1(walletButtonEvent)
    }

    fun trackSettingsTabButton() {
        analyticsLogger.logEventV3Dot1(settingsButtonEvent)
    }

    companion object {
        internal fun makeHomeButtonEvent(context: Context) = with(context) {
            TrackEvent.Icon(
                text = getEnglishString(R.string.app_home),
                params = getRequiredParams(TaxonomyLevel2.HOME)
            )
        }

        internal fun makeWalletButtonEvent(context: Context) = with(context) {
            TrackEvent.Icon(
                text = getEnglishString(R.string.app_wallet),
                params = getRequiredParams(TaxonomyLevel2.WALLET)
            )
        }

        internal fun makeSettingsButtonEvent(context: Context) = with(context) {
            TrackEvent.Icon(
                text = getEnglishString(R.string.app_settingsTitle),
                params = getRequiredParams(TaxonomyLevel2.SETTINGS)
            )
        }

        private fun getRequiredParams(taxonomyL2: TaxonomyLevel2) = RequiredParameters(
            taxonomyLevel2 = taxonomyL2,
            taxonomyLevel3 = TaxonomyLevel3.UNDEFINED
        )
    }
}
