package uk.gov.onelogin.signOut.ui

import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import uk.gov.android.ui.pages.modal.ModalDialog
import uk.gov.android.ui.pages.modal.ModalDialogParameters
import uk.gov.android.ui.theme.m3.GdsTheme
import uk.gov.onelogin.core.meta.ExcludeFromJacocoGeneratedReport
import uk.gov.onelogin.core.meta.ScreenPreview
import uk.gov.onelogin.ui.components.BackHandlerWithPop
import uk.gov.onelogin.ui.loading.LoadingScreen

@Composable
fun SignOutScreen(
    viewModel: SignOutViewModel = hiltViewModel()
) {
    val loading by viewModel.loadingState.collectAsState()
    val analytics: SignOutAnalyticsViewModel = hiltViewModel()
    // Needed for deleteWalletData
    val fragmentActivity = LocalActivity.current as FragmentActivity

    if (loading) {
        LoadingScreen {
            fragmentActivity.finish()
        }
    } else {
        SignOutBody(
            uiState = viewModel.uiState,
            onClose = {
                analytics.trackCloseIcon()
                viewModel.goBack()
            },
            onPrimary = {
                analytics.trackPrimary()
                viewModel.signOut(fragmentActivity)
            }
        )
        analytics.trackSignOutView(viewModel.uiState)
    }

    BackHandlerWithPop {
        analytics.trackBackPressed()
    }
}

@Composable
internal fun SignOutBody(
    uiState: SignOutUIState,
    onPrimary: () -> Unit,
    onClose: () -> Unit
) {
    ModalDialog(
        ModalDialogParameters(
            title = stringResource(id = uiState.title),
            header = buildAnnotatedString {
                append(stringResource(id = uiState.header))
                if (uiState == SignOutUIState.Wallet) {
                    appendLine()
                    appendLine()
                    append(stringResource(id = uiState.subTitle))
                }
            },
            bullets = uiState.bullets.map { stringResource(it) },
            footer = buildAnnotatedString {
                append(stringResource(id = uiState.footer))
            },
            buttonParams = ModalDialogParameters.ButtonParameters(
                text = stringResource(id = uiState.button),
                buttonType = uiState.buttonType,
                isEnabled = true,
                onClick = onPrimary
            ),
            onClose = onClose
        )
    )
}

@ExcludeFromJacocoGeneratedReport
@ScreenPreview
@Composable
internal fun SignOutWalletPreview() {
    GdsTheme {
        SignOutBody(
            uiState = SignOutUIState.Wallet,
            onPrimary = {},
            onClose = {}
        )
    }
}

@ExcludeFromJacocoGeneratedReport
@PreviewLightDark
@Composable
internal fun SignOutPreview() {
    GdsTheme {
        SignOutBody(
            uiState = SignOutUIState.NoWallet,
            onPrimary = {},
            onClose = {}
        )
    }
}
