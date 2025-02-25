package uk.gov.onelogin.features.login.ui.signin.welcome

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import uk.gov.android.onelogin.core.R
import uk.gov.android.ui.components.content.GdsContentText
import uk.gov.android.ui.pages.LandingPage
import uk.gov.android.ui.pages.LandingPageParameters
import uk.gov.android.ui.theme.m3.GdsTheme
import uk.gov.onelogin.core.ui.meta.ExcludeFromJacocoGeneratedReport
import uk.gov.onelogin.core.ui.meta.ScreenPreview
import uk.gov.onelogin.core.ui.pages.loading.LoadingScreen
import uk.gov.onelogin.core.ui.pages.loading.LoadingScreenAnalyticsViewModel
import uk.gov.onelogin.developer.DeveloperTools

@Composable
fun WelcomeScreen(
    viewModel: WelcomeScreenViewModel = hiltViewModel(),
    analyticsViewModel: SignInAnalyticsViewModel = hiltViewModel(),
    loadingAnalyticsViewModel: LoadingScreenAnalyticsViewModel = hiltViewModel(),
    shouldTryAgain: () -> Boolean = { false }
) {
    val loading = viewModel.loading.collectAsState()
    val launcher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.let { intent ->
                    viewModel.handleActivityResult(intent = intent)
                }
            }
        }
    WelcomeBody(
        onSignIn = {
            if (viewModel.onlineChecker.isOnline()) {
                viewModel.onPrimary(launcher)
                analyticsViewModel.trackSignIn()
            } else {
                viewModel.navigateToOfflineError()
            }
        },
        onTopIconClick = {
            if (DeveloperTools.isDeveloperPanelEnabled()) {
                viewModel.navigateToDevPanel()
            }
        }
    )
    if (loading.value) {
        LoadingScreen(loadingAnalyticsViewModel) {
            // Nothing to do
        }
    }

    LaunchedEffect(key1 = Unit) {
        if (!shouldTryAgain()) return@LaunchedEffect
        if (viewModel.onlineChecker.isOnline()) {
            viewModel.onPrimary(launcher)
        } else {
            viewModel.navigateToOfflineError()
        }
    }

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        analyticsViewModel.trackWelcomeView()
    }

    LifecycleEventEffect(Lifecycle.Event.ON_START) {
        viewModel.stopLoading()
    }
}

@Composable
internal fun WelcomeBody(
    onSignIn: () -> Unit = { },
    onTopIconClick: () -> Unit = { }
) {
    LandingPage(
        landingPageParameters = LandingPageParameters(
            content = listOf(
                GdsContentText.GdsContentTextString(
                    text = intArrayOf(
                        R.string.app_signInBody
                    )
                )
            ),
            onPrimary = onSignIn,
            onTopIconClick = onTopIconClick,
            primaryButtonText = R.string.app_signInButton,
            title = R.string.app_signInTitle,
            topIcon = R.drawable.app_icon,
            contentDescription = R.string.app_signInIconDescription
        )
    )
}

@ExcludeFromJacocoGeneratedReport
@ScreenPreview
@Composable
internal fun WelcomePreview() {
    GdsTheme {
        WelcomeBody()
    }
}
