package uk.gov.onelogin.login.ui.splash

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uk.gov.android.onelogin.R
import uk.gov.android.ui.components.GdsHeading
import uk.gov.android.ui.components.HeadingParameters
import uk.gov.android.ui.components.HeadingSize
import uk.gov.android.ui.components.images.icon.IconParameters
import uk.gov.android.ui.components.m3.images.icon.GdsIcon
import uk.gov.android.ui.theme.GdsTheme
import uk.gov.android.ui.theme.mediumPadding
import uk.gov.android.ui.theme.smallPadding
import uk.gov.onelogin.core.meta.ExcludeFromJacocoGeneratedReport
import uk.gov.onelogin.developer.DeveloperTools
import uk.gov.onelogin.optin.ui.OptInRequirementViewModel

@Composable
fun SplashScreen(
    viewModel: SplashScreenViewModel = hiltViewModel()
) {
    val context = LocalActivity.current as FragmentActivity
    val lifecycleOwner = LocalLifecycleOwner.current
    val optInRequirementViewModel: OptInRequirementViewModel = hiltViewModel()
    val loading = viewModel.loading.collectAsState()

    SplashBody(
        isUnlock = viewModel.showUnlock.value,
        loading = loading.value,
        onLogin = { viewModel.login(context) },
        onOpenDeveloperPortal = { viewModel.navigateToDevPanel() }
    )

    DisposableEffect(key1 = lifecycleOwner) {
        with(lifecycleOwner) {
            lifecycle.addObserver(viewModel)
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.RESUMED) {
                    viewModel.retrieveAppInfo {
                        optInRequirementViewModel.isOptInRequired.collectLatest { isRequired ->
                            when {
                                isRequired -> viewModel.navigateToAnalyticsOptIn()
                                else -> if (!viewModel.showUnlock.value) {
                                    viewModel.login(context)
                                }
                            }
                        }
                    }
                }
            }
            onDispose {
                lifecycleOwner.lifecycle.removeObserver(viewModel)
            }
        }
    }
}

@Composable
internal fun SplashBody(
    isUnlock: Boolean,
    loading: Boolean,
    onLogin: () -> Unit,
    onOpenDeveloperPortal: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.govuk_blue)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        GdsIcon(
            parameters = IconParameters(
                image = R.drawable.tudor_crown_with_gov_uk,
                backGroundColor = colorResource(id = R.color.govuk_blue),
                foreGroundColor = Color.White,
                modifier = Modifier
                    .weight(1F)
                    .clickable(enabled = DeveloperTools.isDeveloperPanelEnabled()) {
                        onOpenDeveloperPortal()
                    }
                    .testTag(stringResource(id = R.string.splashIconTestTag))
            )
        )
        if (isUnlock && !loading) {
            GdsHeading(
                headingParameters = HeadingParameters(
                    size = HeadingSize.H3(),
                    text = R.string.app_unlockButton,
                    color = Color.White,
                    backgroundColor = colorResource(id = R.color.govuk_blue),
                    modifier = Modifier
                        .clickable {
                            onLogin()
                        }
                        .padding(bottom = mediumPadding)
                )
            )
        }
        if (loading && !isUnlock) {
            LoadingIndicator()
        }
    }
}

@Composable
internal fun LoadingIndicator() {
    val loadingContentDescription = stringResource(
        id = R.string.app_splashScreenLoadingContentDescription
    )
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = smallPadding,
                end = smallPadding,
                bottom = PROGRESS_BAR
            )
            .semantics { contentDescription = loadingContentDescription }
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = smallPadding)
        ) {
            CircularProgressIndicator(
                color = colorResource(id = R.color.govuk_blue),
                trackColor = MaterialTheme.colorScheme.onPrimary,
                strokeCap = StrokeCap.Square,
                modifier = Modifier
                    .width(PROGRESS_BAR)
                    .height(PROGRESS_BAR)
            )
        }
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(id = R.string.app_splashScreenLoadingIndicatorText),
                color = Color.White
            )
        }
    }
}

@ExcludeFromJacocoGeneratedReport
@PreviewScreenSizes
@Composable
internal fun SplashScreenPreview() {
    GdsTheme {
        SplashBody(
            isUnlock = false,
            loading = false,
            onLogin = {},
            onOpenDeveloperPortal = {}
        )
    }
}

@ExcludeFromJacocoGeneratedReport
@Preview
@Composable
internal fun UnlockScreenPreview() {
    GdsTheme {
        SplashBody(
            isUnlock = true,
            loading = false,
            onLogin = {},
            onOpenDeveloperPortal = {}
        )
    }
}

@ExcludeFromJacocoGeneratedReport
@Preview
@Composable
internal fun LoadingSplashScreenPreview() {
    GdsTheme {
        SplashBody(
            isUnlock = false,
            loading = true,
            onLogin = {},
            onOpenDeveloperPortal = {}
        )
    }
}

val PROGRESS_BAR = 48.dp
