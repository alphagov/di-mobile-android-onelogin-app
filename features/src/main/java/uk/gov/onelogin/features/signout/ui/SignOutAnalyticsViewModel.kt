package uk.gov.onelogin.features.signout.ui

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
import uk.gov.onelogin.features.signout.domain.SignOutUIState

@HiltViewModel
class SignOutAnalyticsViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val analyticsLogger: AnalyticsLogger
) : ViewModel() {
    private val onPrimaryEvent = onPrimaryEvent(context)
    private val onCloseIcon = onCloseIcon()
    private val onBackPressed = onBackPressed()
    private val signOutNoWalletViewEvent = makeSignOutNoWalletViewEvent(context)
    private val signOutWalletViewEvent = makeSignOutWalletViewEvent(context)

    fun trackPrimary() {
        analyticsLogger.logEventV3Dot1(onPrimaryEvent)
    }

    fun trackCloseIcon() {
        analyticsLogger.logEventV3Dot1(onCloseIcon)
    }

    fun trackBackPressed() {
        analyticsLogger.logEventV3Dot1(onBackPressed)
    }

    fun trackSignOutView(uiState: SignOutUIState) {
        if (uiState == SignOutUIState.Wallet) {
            analyticsLogger.logEventV3Dot1(signOutWalletViewEvent)
        } else {
            analyticsLogger.logEventV3Dot1(signOutNoWalletViewEvent)
        }
    }

    companion object {
        fun onPrimaryEvent(context: Context) = with(context) {
            TrackEvent.Button(
                text = getEnglishString(R.string.app_signOutAndDeleteAppDataButton),
                params =
                RequiredParameters(
                    taxonomyLevel2 = TaxonomyLevel2.ACCOUNT,
                    taxonomyLevel3 = TaxonomyLevel3.SIGN_OUT
                )
            )
        }

        fun onCloseIcon() = TrackEvent.Icon(
            text = "back",
            params =
            RequiredParameters(
                taxonomyLevel2 = TaxonomyLevel2.ACCOUNT,
                taxonomyLevel3 = TaxonomyLevel3.SIGN_OUT
            )
        )

        fun onBackPressed() = TrackEvent.Icon(
            text = "back - system",
            params =
            RequiredParameters(
                taxonomyLevel2 = TaxonomyLevel2.ACCOUNT,
                taxonomyLevel3 = TaxonomyLevel3.SIGN_OUT
            )
        )

        fun makeSignOutWalletViewEvent(context: Context) = with(context) {
            ViewEvent.Screen(
                name = getEnglishString(R.string.app_signOutConfirmationTitle),
                id = getEnglishString(R.string.sign_out_wallet_page_id),
                params =
                RequiredParameters(
                    taxonomyLevel2 = TaxonomyLevel2.ACCOUNT,
                    taxonomyLevel3 = TaxonomyLevel3.SIGN_OUT
                )
            )
        }

        fun makeSignOutNoWalletViewEvent(context: Context) = with(context) {
            ViewEvent.Screen(
                name = getEnglishString(R.string.app_signOutConfirmationTitle),
                id = getEnglishString(R.string.sign_out_no_wallet_page_id),
                params =
                RequiredParameters(
                    taxonomyLevel2 = TaxonomyLevel2.ACCOUNT,
                    taxonomyLevel3 = TaxonomyLevel3.SIGN_OUT
                )
            )
        }
    }
}
