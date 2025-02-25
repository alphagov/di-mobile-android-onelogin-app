package uk.gov.onelogin.features.settings.ui

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
class SettingsAnalyticsViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val analyticsLogger: AnalyticsLogger
) : ViewModel() {
    private val settingsViewEvent = makeSettingsViewEvent(context)
    private val backEvent = makeBackEvent(context)
    private val signInDetailEvent = makeSignInDetailsEvent(context)
    private val usingOneLoginEvent = makeUsingOneLoginEvent(context)
    private val contactEvent = makeContactEvent(context)
    private val privacyNoticeEvent = makePrivacyNoticeEvent(context)
    private val accessibilityStatementEvent = makeAccessibilityEvent(context)
    private val openSourceEvent = makeOpenSourceEvent(context)
    private val signOutEvent = makeSignOutEvent(context)

    fun trackSettingsView() {
        analyticsLogger.logEventV3Dot1(settingsViewEvent)
    }

    fun trackBackButton() {
        analyticsLogger.logEventV3Dot1(backEvent)
    }

    fun trackSignInDetailLink() {
        analyticsLogger.logEventV3Dot1(signInDetailEvent)
    }

    fun trackUsingOneLoginLink() {
        analyticsLogger.logEventV3Dot1(usingOneLoginEvent)
    }

    fun trackContactOneLoginLink() {
        analyticsLogger.logEventV3Dot1(contactEvent)
    }

    fun trackPrivacyNoticeLink() {
        analyticsLogger.logEventV3Dot1(privacyNoticeEvent)
    }

    fun trackAccessibilityStatementLink() {
        analyticsLogger.logEventV3Dot1(accessibilityStatementEvent)
    }

    fun trackOpenSourceButton() {
        analyticsLogger.logEventV3Dot1(openSourceEvent)
    }

    fun trackSignOutButton() {
        analyticsLogger.logEventV3Dot1(signOutEvent)
    }

    companion object {
        internal fun makeBackEvent(context: Context) = with(context) {
            TrackEvent.Icon(
                text = getEnglishString(R.string.system_backButton),
                params = requiredParams
            )
        }

        internal fun makeSettingsViewEvent(context: Context) = with(context) {
            ViewEvent.Screen(
                name = getEnglishString(R.string.action_settings),
                id = getEnglishString(R.string.settings_page_id),
                params = requiredParams
            )
        }

        internal fun makeSignInDetailsEvent(context: Context) = with(context) {
            TrackEvent.Link(
                isExternal = false,
                domain = getEnglishString(R.string.app_manageSignInDetailsUrl, "").domain,
                text = getEnglishString(R.string.app_settingsSignInDetailsLink),
                params = requiredParams
            )
        }

        internal fun makeUsingOneLoginEvent(context: Context) = with(context) {
            TrackEvent.Link(
                isExternal = false,
                domain = getEnglishString(R.string.app_oneLoginCardLinkUrl, "").domain,
                text = getEnglishString(R.string.app_appGuidanceLink),
                params = requiredParams
            )
        }

        internal fun makeContactEvent(context: Context) = with(context) {
            TrackEvent.Link(
                isExternal = false,
                domain = getEnglishString(R.string.app_contactUrl, "").domain,
                text = getEnglishString(R.string.app_contactLink),
                params = requiredParams
            )
        }

        internal fun makePrivacyNoticeEvent(context: Context) = with(context) {
            TrackEvent.Link(
                isExternal = false,
                domain = getEnglishString(R.string.privacy_notice_url, "").domain,
                text = getEnglishString(R.string.app_privacyNoticeLink2),
                params = requiredParams
            )
        }

        internal fun makeAccessibilityEvent(context: Context) = with(context) {
            TrackEvent.Link(
                isExternal = false,
                domain = getEnglishString(R.string.app_accessibilityStatementUrl, "").domain,
                text = getEnglishString(R.string.app_accessibilityStatement),
                params = requiredParams
            )
        }

        internal fun makeOpenSourceEvent(context: Context) = with(context) {
            TrackEvent.Button(
                text = getEnglishString(R.string.app_openSourceLicences),
                params = requiredParams
            )
        }

        internal fun makeSignOutEvent(context: Context) = with(context) {
            TrackEvent.Button(
                text = getEnglishString(R.string.app_signOutButton),
                params = requiredParams
            )
        }

        private val requiredParams = RequiredParameters(
            taxonomyLevel2 = TaxonomyLevel2.SETTINGS,
            taxonomyLevel3 = TaxonomyLevel3.UNDEFINED
        )
    }
}
