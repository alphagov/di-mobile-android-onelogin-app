package uk.gov.onelogin.features.login.ui.signin

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import uk.gov.android.onelogin.core.R
import uk.gov.android.ui.components.content.GdsContentText
import uk.gov.android.ui.pages.LandingPage
import uk.gov.android.ui.pages.LandingPageParameters
import uk.gov.android.ui.theme.GdsTheme

@Composable
@Preview
fun PasscodeInfoScreen(onPrimary: () -> Unit = {}) {
    GdsTheme {
        LandingPage(
            landingPageParameters = LandingPageParameters(
                title = R.string.app_noPasscodePatternSetupTitle,
                content = listOf(
                    GdsContentText.GdsContentTextString(
                        text = intArrayOf(R.string.app_noPasscodeSetupBody1)
                    ),
                    GdsContentText.GdsContentTextString(
                        text = intArrayOf(R.string.app_noPasscodeSetupBody2)
                    )
                ),
                primaryButtonText = R.string.app_continue,
                topIcon = R.drawable.passcode_info,
                onPrimary = onPrimary
            )
        )
    }
}
