package uk.gov.onelogin.features.signout.ui

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

@HiltViewModel
class SignedOutInfoAnalyticsViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val analyticsLogger: AnalyticsLogger
) : ViewModel() {
    private val reAuthEvent = makeReAuthEvent(context)
    private val signedOutInfoViewEvent = makeSignedOutInfoViewEvent(context)

    fun trackReAuth() {
        analyticsLogger.logEventV3Dot1(reAuthEvent)
    }

    fun trackSignOutInfoView() {
        analyticsLogger.logEventV3Dot1(signedOutInfoViewEvent)
    }

    companion object {
        fun makeReAuthEvent(context: Context) = with(context) {
            TrackEvent.Link(
                isExternal = false,
                domain = getEnglishString(R.string.baseStsUrl, "").domain,
                text = getEnglishString(R.string.app_SignInWithGovUKOneLoginButton),
                params = RequiredParameters(
                    taxonomyLevel2 = TaxonomyLevel2.LOGIN,
                    taxonomyLevel3 = TaxonomyLevel3.RE_AUTH
                )
            )
        }

        fun makeSignedOutInfoViewEvent(context: Context) = with(context) {
            ViewEvent.Screen(
                name = getEnglishString(R.string.app_youveBeenSignedOutTitle),
                id = getEnglishString(R.string.signed_out_info_page_id),
                params = RequiredParameters(
                    taxonomyLevel2 = TaxonomyLevel2.LOGIN,
                    taxonomyLevel3 = TaxonomyLevel3.RE_AUTH
                )
            )
        }
    }
}
