package uk.gov.onelogin.features.login.ui.signin.welcome

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
class SignInAnalyticsViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val analyticsLogger: AnalyticsLogger
) : ViewModel() {
    private val signInEvent = makeSignInEvent(context)
    private val welcomeViewEvent = makeWelcomeViewEvent(context)

    fun trackSignIn() {
        analyticsLogger.logEventV3Dot1(signInEvent)
    }

    fun trackWelcomeView() {
        analyticsLogger.logEventV3Dot1(welcomeViewEvent)
    }

    companion object {
        fun makeSignInEvent(context: Context) = with(context) {
            TrackEvent.Link(
                isExternal = false,
                domain = getEnglishString(R.string.baseStsUrl, "").domain,
                text = getEnglishString(R.string.app_signInButton),
                params = RequiredParameters(
                    taxonomyLevel2 = TaxonomyLevel2.LOGIN,
                    taxonomyLevel3 = TaxonomyLevel3.SIGN_IN
                )
            )
        }

        fun makeWelcomeViewEvent(context: Context) = with(context) {
            ViewEvent.Screen(
                name = getEnglishString(R.string.app_signInTitle),
                id = getEnglishString(R.string.sign_in_page_id),
                params = RequiredParameters(
                    taxonomyLevel2 = TaxonomyLevel2.LOGIN,
                    taxonomyLevel3 = TaxonomyLevel3.SIGN_IN
                )
            )
        }
    }
}
