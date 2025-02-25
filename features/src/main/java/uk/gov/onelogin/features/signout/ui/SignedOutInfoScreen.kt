package uk.gov.onelogin.features.signout.ui

import android.app.Activity
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import uk.gov.android.onelogin.core.R
import uk.gov.android.ui.components.content.GdsContentText
import uk.gov.android.ui.pages.LandingPage
import uk.gov.android.ui.pages.LandingPageParameters
import uk.gov.android.ui.theme.m3.GdsTheme
import uk.gov.android.ui.theme.smallPadding
import uk.gov.onelogin.core.ui.meta.ExcludeFromJacocoGeneratedReport
import uk.gov.onelogin.core.ui.meta.ScreenPreview
import uk.gov.onelogin.core.ui.pages.loading.LoadingScreen
import uk.gov.onelogin.core.ui.pages.loading.LoadingScreenAnalyticsViewModel
import uk.gov.onelogin.features.login.ui.signin.welcome.WelcomeScreenViewModel

@Composable
fun SignedOutInfoScreen(
    loginViewModel: WelcomeScreenViewModel = hiltViewModel(),
    signOutViewModel: SignedOutInfoViewModel = hiltViewModel(),
    analyticsViewModel: SignedOutInfoAnalyticsViewModel = hiltViewModel(),
    loadingAnalyticsViewModel: LoadingScreenAnalyticsViewModel = hiltViewModel(),
    shouldTryAgain: () -> Boolean = { false }
) {
    val loading by loginViewModel.loading.collectAsState()
    val activity = LocalContext.current as FragmentActivity
    val launcher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.let { intent ->
                    loginViewModel.handleActivityResult(
                        intent = intent,
                        isReAuth = signOutViewModel.shouldReAuth()
                    )
                    signOutViewModel.saveTokens()
                }
            }
        }

    LaunchedEffect(key1 = Unit) {
        signOutViewModel.resetTokens()

        if (!shouldTryAgain()) return@LaunchedEffect

        handleLogin(
            loginViewModel,
            signOutViewModel,
            launcher,
            activity
        )
    }

    if (loading) {
        LoadingScreen(loadingAnalyticsViewModel) {}
    } else {
        SignedOutInfoBody {
            analyticsViewModel.trackReAuth()
            handleLogin(
                loginViewModel,
                signOutViewModel,
                launcher,
                activity
            )
        }
    }

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        analyticsViewModel.trackSignOutInfoView()
    }

    LifecycleEventEffect(Lifecycle.Event.ON_START) {
        loginViewModel.stopLoading()
    }
}

private fun handleLogin(
    loginViewModel: WelcomeScreenViewModel,
    signOutViewModel: SignedOutInfoViewModel,
    launcher: ActivityResultLauncher<Intent>,
    activity: FragmentActivity
) {
    if (loginViewModel.onlineChecker.isOnline()) {
        signOutViewModel.checkPersistentId(activity) {
            loginViewModel.onPrimary(launcher)
        }
    } else {
        loginViewModel.navigateToOfflineError()
    }
}

@Composable
internal fun SignedOutInfoBody(onPrimary: () -> Unit) {
    LandingPage(
        landingPageParameters = LandingPageParameters(
            title = R.string.app_youveBeenSignedOutTitle,
            titleBottomPadding = smallPadding,
            content = listOf(
                GdsContentText.GdsContentTextString(
                    intArrayOf(R.string.app_youveBeenSignedOutBody1)
                ),
                GdsContentText.GdsContentTextString(
                    intArrayOf(R.string.app_youveBeenSignedOutBody2)
                )
            ),
            contentInternalPadding = PaddingValues(bottom = smallPadding),
            primaryButtonText = R.string.app_SignInWithGovUKOneLoginButton,
            onPrimary = onPrimary
        )
    )
}

@ExcludeFromJacocoGeneratedReport
@ScreenPreview
@Composable
internal fun SignedOutInfoPreview() {
    GdsTheme {
        SignedOutInfoBody {}
    }
}
