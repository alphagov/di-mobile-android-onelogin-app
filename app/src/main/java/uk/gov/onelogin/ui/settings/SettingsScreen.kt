package uk.gov.onelogin.ui.settings

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import uk.gov.android.onelogin.R
import uk.gov.android.ui.components.GdsHeading
import uk.gov.android.ui.components.HeadingParameters
import uk.gov.android.ui.components.HeadingSize
import uk.gov.android.ui.theme.mediumPadding
import uk.gov.android.ui.theme.smallPadding
import uk.gov.onelogin.optin.ui.PrivacyNotice
import uk.gov.onelogin.ui.components.EmailSection
import uk.gov.onelogin.ui.components.TitledPage

@Composable
@Preview
fun SettingsScreen(
    viewModel: SettingsScreenViewModel = hiltViewModel()
) {
    val uriHandler = LocalUriHandler.current
    val email = viewModel.email
    val optInState by viewModel.optInState.collectAsStateWithLifecycle(false)
    val signInUrl = stringResource(R.string.app_manageSignInDetailsUrl)
    val privacyNoticeUrl = stringResource(R.string.privacy_notice_url)
    val accessibilityStatementUrl = stringResource(R.string.app_accessibilityStatementUrl)
    val helpUrl = stringResource(R.string.app_helpUrl)
    val contactUrl = stringResource(R.string.app_contactUrl)
    TitledPage(
        title = R.string.app_settingsTitle
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            EmailSection(email)
            YourDetailsSection(uriHandler, signInUrl)
            HelpAndFeedbackSection(uriHandler, helpUrl, contactUrl)
            AboutTheAppSection(
                optInState,
                uriHandler,
                privacyNoticeUrl
            ) {
                viewModel.toggleOptInPreference()
            }
            LegalSection(uriHandler, privacyNoticeUrl, accessibilityStatementUrl)
            SignOutRow { viewModel.goToSignOut() }
        }
    }
}

@Composable
private fun YourDetailsSection(
    uriHandler: UriHandler,
    signInUrl: String
) {
    HorizontalDivider()
    ExternalLinkRow(
        R.string.app_settingsSignInDetailsLink,
        R.drawable.external_link_icon,
        description = stringResource(
            id = R.string.app_settingSignInDetailsFootnote
        )
    ) {
        uriHandler.openUri(signInUrl)
    }
}

@Composable
private fun LegalSection(
    uriHandler: UriHandler,
    privacyNoticeUrl: String,
    accessibilityStatementUrl: String
) {
    ExternalLinkRow(R.string.app_privacyNoticeLink2, R.drawable.external_link_icon) {
        uriHandler.openUri(privacyNoticeUrl)
    }
    HorizontalDivider()
    ExternalLinkRow(R.string.app_accessibilityStatement, R.drawable.external_link_icon) {
        uriHandler.openUri(accessibilityStatementUrl)
    }
    HorizontalDivider()
    ExternalLinkRow(R.string.app_openSourceLicences, R.drawable.arrow_right_icon)
}

@Composable
private fun HelpAndFeedbackSection(
    uriHandler: UriHandler,
    helpUrl: String,
    contactUrl: String
) {
    HeadingRow(R.string.app_settingsSubtitle1)
    ExternalLinkRow(
        R.string.app_appGuidanceLink,
        R.drawable.external_link_icon
    ) {
        uriHandler.openUri(helpUrl)
    }
    HorizontalDivider()
    ExternalLinkRow(
        R.string.app_contactLink,
        R.drawable.external_link_icon
    ) {
        uriHandler.openUri(contactUrl)
    }
}

@Composable
internal fun AboutTheAppSection(
    optInState: Boolean,
    uriHandler: UriHandler,
    privacyNoticeUrl: String,
    onToggle: () -> Unit
) {
    HeadingRow(R.string.app_settingsSubtitle2)
    PreferenceToggleRow(
        title = R.string.app_settingsAnalyticsToggle,
        checked = optInState,
        onToggle = { onToggle() }
    )
    PrivacyNotice(
        Modifier
            .padding(smallPadding),
        style = MaterialTheme.typography.bodySmall,
        privacyNoticeString = stringResource(
            id = R.string.app_settingsAnalyticsToggleFootnote
        ),
        privacyNoticeLink = stringResource(
            id = R.string.app_settingsAnalyticsToggleFootnoteLink
        )
    ) {
        uriHandler.openUri(privacyNoticeUrl)
    }
}

@Composable
private fun HeadingRow(@androidx.annotation.StringRes text: Int) {
    GdsHeading(
        HeadingParameters(
            text = text,
            size = HeadingSize.H3(),
            textAlign = TextAlign.Center,
            backgroundColor = MaterialTheme.colorScheme.background,
            padding = PaddingValues(all = smallPadding)
        )
    )
}

@Composable
private fun ExternalLinkRow(
    @androidx.annotation.StringRes title: Int,
    @DrawableRes icon: Int,
    description: String? = null,
    onClick: () -> Unit = { }
) {
    Box(
        modifier = Modifier
            .clickable(onClick = onClick)
            .background(color = MaterialTheme.colorScheme.inverseOnSurface)
            .fillMaxWidth()
    ) {
        Column {
            Text(
                modifier = Modifier
                    .padding(horizontal = smallPadding)
                    .padding(top = smallPadding)
                    .padding(bottom = if (description == null) smallPadding else 4.dp),
                style = MaterialTheme.typography.bodyMedium,
                text = stringResource(title)
            )
            description?.let {
                Text(
                    modifier = Modifier
                        .padding(
                            start = smallPadding,
                            bottom = smallPadding,
                            end = 64.dp
                        ),
                    style = MaterialTheme.typography.bodySmall,
                    text = it
                )
            }
        }
        Icon(
            painter = painterResource(id = icon),
            contentDescription = "",
            modifier = Modifier
                .padding(end = smallPadding, top = smallPadding)
                .size(24.dp)
                .align(alignment = Alignment.TopEnd)
        )
    }
}

@Composable()
internal fun PreferenceToggleRow(
    @androidx.annotation.StringRes title: Int,
    checked: Boolean,
    onToggle: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .height(56.dp)
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.inverseOnSurface)
            .semantics {
                role = Role.Switch
                onClick {
                    onToggle()
                    true
                }
            }
            .focusable()
            .padding(
                start = smallPadding,
                end = smallPadding
            )
    ) {
        Text(
            modifier = Modifier.weight(1F),
            text = stringResource(title),
            style = MaterialTheme.typography.bodyMedium
        )
        Switch(
            checked = checked,
            onCheckedChange = { onToggle() },
            modifier = Modifier
                .testTag(stringResource(id = R.string.optInSwitchTestTag))
                .clearAndSetSemantics { }
        )
    }
    HorizontalDivider()
}

@Composable
private fun SignOutRow(openSignOutScreen: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .padding(top = mediumPadding)
            .height(56.dp)
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.inverseOnSurface)
            .clickable {
                openSignOutScreen()
            }
    ) {
        Text(
            modifier = Modifier
                .padding(all = smallPadding)
                .height(24.dp),
            style = MaterialTheme.typography.bodyMedium,
            text = stringResource(R.string.app_signOutButton),
            color = MaterialTheme.colorScheme.primary
        )
    }
}
