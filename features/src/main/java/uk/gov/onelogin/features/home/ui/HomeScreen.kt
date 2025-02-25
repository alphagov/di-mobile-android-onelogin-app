package uk.gov.onelogin.features.home.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import uk.gov.android.onelogin.core.R
import uk.gov.android.ui.componentsv2.GdsCard
import uk.gov.android.ui.theme.smallPadding
import uk.gov.onelogin.core.ui.pages.TitledPage
import uk.gov.onelogin.criorchestrator.features.resume.publicapi.ProveYourIdentityCard
import uk.gov.onelogin.criorchestrator.sdk.publicapi.rememberCriOrchestrator
import uk.gov.onelogin.developer.DeveloperTools

@Composable
@Preview
fun HomeScreen(
    viewModel: HomeScreenViewModel = hiltViewModel(),
    analyticsViewModel: HomeScreenAnalyticsViewModel = hiltViewModel()
) {
    val httpClient = viewModel.httpClient
    val analyticsLogger = viewModel.analyticsLogger
    val criOrchestratorComponent = rememberCriOrchestrator(httpClient, analyticsLogger)
    val contentsCardTitle = stringResource(R.string.app_oneLoginCardTitle)
    val contentsCardBody = stringResource(R.string.app_oneLoginCardBody)
    val contentsCardLinkText = stringResource(R.string.app_oneLoginCardLink)
    val servicesUrl = stringResource(R.string.app_oneLoginCardLinkUrl)
    val uriHandler = LocalUriHandler.current
    BackHandler { analyticsViewModel.trackBackButton() }
    LaunchedEffect(Unit) {
        viewModel.getUiCardFlagState()
        analyticsViewModel.trackScreen()
    }
    TitledPage(R.string.app_homeTitle) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = smallPadding)
                .verticalScroll(rememberScrollState())
                .height(IntrinsicSize.Max)
        ) {
            if (viewModel.uiCardEnabled.collectAsState().value) {
                Row(
                    modifier = Modifier
                        .testTag(stringResource(R.string.appCriCardTestTag))
                ) {
                    ProveYourIdentityCard(
                        component = criOrchestratorComponent,
                        modifier = Modifier
                    )
                }
            }
            GdsCard(
                title = contentsCardTitle,
                body = contentsCardBody,
                buttonText = contentsCardLinkText,
                displayPrimary = false,
                showSecondaryIcon = true,
                onClick = {
                    analyticsViewModel.trackLink()
                    uriHandler.openUri(servicesUrl)
                },
                modifier = Modifier
                    .padding(top = smallPadding)
                    .testTag(stringResource(R.string.yourServicesCardTestTag))
            )
            if (DeveloperTools.isDeveloperPanelEnabled()) {
                TextButton(
                    onClick = { viewModel.openDevPanel() }
                ) {
                    Text("Developer Panel")
                }
            }
        }
    }
}
