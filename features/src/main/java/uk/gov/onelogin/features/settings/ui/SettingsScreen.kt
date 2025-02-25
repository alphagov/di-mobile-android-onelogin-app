package uk.gov.onelogin.features.settings.ui

import androidx.activity.compose.BackHandler
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
import androidx.compose.runtime.LaunchedEffect
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
import uk.gov.android.onelogin.core.R
import uk.gov.android.ui.components.GdsHeading
import uk.gov.android.ui.components.HeadingParameters
import uk.gov.android.ui.components.HeadingSize
import uk.gov.android.ui.theme.mediumPadding
import uk.gov.android.ui.theme.smallPadding
import uk.gov.onelogin.core.ui.components.EmailSection
import uk.gov.onelogin.core.ui.pages.TitledPage
import uk.gov.onelogin.features.optin.ui.PrivacyNotice

@Composable
@Preview
fun SettingsScreen(
    viewModel: SettingsScreenViewModel = hiltViewModel(),
    analyticsViewModel: SettingsAnalyticsViewModel = hiltViewModel()
) {
    BackHandler { analyticsViewModel.trackBackButton() }
    LaunchedEffect(Unit) {
        analyticsViewModel.trackSettingsView()
    }
    val uriHandler = LocalUriHandler.current
    val email = viewModel.email
    val optInState by viewModel.optInState.collectAsStateWithLifecycle(false)
    val settingsScreenLinks = SettingsScreenLinks(
        signInUrl = stringResource(R.string.app_manageSignInDetailsUrl),
        privacyNoticeUrl = stringResource(R.string.privacy_notice_url),
        accessibilityStatementUrl = stringResource(R.string.app_accessibilityStatementUrl),
        helpUrl = stringResource(R.string.app_helpUrl),
        contactUrl = stringResource(R.string.app_contactUrl)
    )

    TitledPage(title = R.string.app_settingsTitle) { paddingValues ->
        SettingsScreenContent(
            paddingValues = paddingValues,
            email = email,
            optInState = optInState,
            viewModel = viewModel,
            analyticsViewModel = analyticsViewModel,
            uriHandler = uriHandler,
            settingsScreenLinks = settingsScreenLinks
        )
    }
}

@Composable
private fun SettingsScreenContent(
    paddingValues: PaddingValues,
    email: String,
    optInState: Boolean,
    viewModel: SettingsScreenViewModel,
    analyticsViewModel: SettingsAnalyticsViewModel,
    uriHandler: UriHandler,
    settingsScreenLinks: SettingsScreenLinks
) {
    Column(
        modifier = Modifier
            .padding(paddingValues)
            .verticalScroll(rememberScrollState())
    ) {
        EmailSection(email)
        YourDetailsSection(
            onClick = {
                analyticsViewModel.trackSignInDetailLink()
                uriHandler.openUri(settingsScreenLinks.signInUrl)
            }
        )
        HelpAndFeedbackSection(
            onHelpClick = {
                analyticsViewModel.trackUsingOneLoginLink()
                uriHandler.openUri(settingsScreenLinks.helpUrl)
            },
            onContactClick = {
                analyticsViewModel.trackContactOneLoginLink()
                uriHandler.openUri(settingsScreenLinks.contactUrl)
            }
        )
        AboutTheAppSection(
            optInState = optInState,
            onToggle = {
                viewModel.toggleOptInPreference()
            },
            onPrivacyNoticeClick = {
                analyticsViewModel.trackPrivacyNoticeLink()
                uriHandler.openUri(settingsScreenLinks.privacyNoticeUrl)
            }
        )
        LegalSection(
            onPrivacyNoticeClick = {
                analyticsViewModel.trackPrivacyNoticeLink()
                uriHandler.openUri(settingsScreenLinks.privacyNoticeUrl)
            },
            onAccessibilityStatementClick = {
                analyticsViewModel.trackAccessibilityStatementLink()
                uriHandler.openUri(settingsScreenLinks.accessibilityStatementUrl)
            },
            onOpenSourceLicensesClick = {
                analyticsViewModel.trackOpenSourceButton()
            }
        )
        SignOutRow(
            openSignOutScreen = {
                analyticsViewModel.trackSignOutButton()
                viewModel.goToSignOut()
            }
        )
    }
}

private data class SettingsScreenLinks(
    val signInUrl: String,
    val privacyNoticeUrl: String,
    val accessibilityStatementUrl: String,
    val helpUrl: String,
    val contactUrl: String
)

@Composable
private fun YourDetailsSection(
    onClick: () -> Unit
) {
    HorizontalDivider()
    ExternalLinkRow(
        R.string.app_settingsSignInDetailsLink,
        R.drawable.external_link_icon,
        description = stringResource(
            id = R.string.app_settingSignInDetailsFootnote
        )
    ) {
        onClick()
    }
}

@Composable
private fun LegalSection(
    onPrivacyNoticeClick: () -> Unit,
    onAccessibilityStatementClick: () -> Unit,
    onOpenSourceLicensesClick: () -> Unit
) {
    ExternalLinkRow(R.string.app_privacyNoticeLink2, R.drawable.external_link_icon) {
        onPrivacyNoticeClick()
    }
    HorizontalDivider()
    ExternalLinkRow(R.string.app_accessibilityStatement, R.drawable.external_link_icon) {
        onAccessibilityStatementClick()
    }
    HorizontalDivider()
    ExternalLinkRow(R.string.app_openSourceLicences, R.drawable.arrow_right_icon) {
        onOpenSourceLicensesClick()
    }
}

@Composable
private fun HelpAndFeedbackSection(
    onHelpClick: () -> Unit,
    onContactClick: () -> Unit
) {
    HeadingRow(R.string.app_settingsSubtitle1)
    ExternalLinkRow(
        R.string.app_appGuidanceLink,
        R.drawable.external_link_icon
    ) {
        onHelpClick()
    }
    HorizontalDivider()
    ExternalLinkRow(
        R.string.app_contactLink,
        R.drawable.external_link_icon
    ) {
        onContactClick()
    }
}

@Composable
internal fun AboutTheAppSection(
    optInState: Boolean,
    onToggle: () -> Unit,
    onPrivacyNoticeClick: () -> Unit
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
        style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.surface),
        privacyNoticeString = stringResource(
            id = R.string.app_settingsAnalyticsToggleFootnote
        ),
        privacyNoticeLink = stringResource(
            id = R.string.app_settingsAnalyticsToggleFootnoteLink
        )
    ) {
        onPrivacyNoticeClick()
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
                    color = MaterialTheme.colorScheme.surface,
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
