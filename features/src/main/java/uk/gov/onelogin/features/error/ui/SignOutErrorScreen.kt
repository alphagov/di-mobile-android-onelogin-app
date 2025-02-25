package uk.gov.onelogin.features.error.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import uk.gov.android.onelogin.core.R
import uk.gov.android.ui.components.HeadingSize
import uk.gov.android.ui.components.buttons.ButtonParameters
import uk.gov.android.ui.components.buttons.ButtonType
import uk.gov.android.ui.components.content.ContentParameters
import uk.gov.android.ui.components.content.GdsContentText
import uk.gov.android.ui.components.images.icon.IconParameters
import uk.gov.android.ui.components.information.InformationParameters
import uk.gov.android.ui.pages.errors.ErrorPage
import uk.gov.android.ui.pages.errors.ErrorPageParameters
import uk.gov.android.ui.theme.GdsTheme
import uk.gov.android.ui.theme.meta.ExcludeFromJacocoGeneratedReport
import uk.gov.android.ui.theme.meta.ScreenPreview

@Composable
fun SignOutErrorScreen(onExitAppClicked: () -> Unit) {
    GdsTheme {
        ErrorPage(
            parameters = ErrorPageParameters(
                primaryButtonParameters = ButtonParameters(
                    buttonType = ButtonType.PRIMARY(),
                    onClick = onExitAppClicked,
                    text = R.string.app_exitButton
                ),
                informationParameters = InformationParameters(
                    contentParameters = ContentParameters(
                        resource = listOf(
                            GdsContentText.GdsContentTextString(
                                subTitle = R.string.app_signOutErrorTitle,
                                text =
                                intArrayOf(
                                    R.string.app_signOutErrorBody
                                )
                            )
                        ),
                        headingSize = HeadingSize.H1()
                    ),
                    iconParameters = IconParameters(
                        foreGroundColor = Color.Unspecified,
                        image = uk.gov.android.ui.components.R.drawable.ic_error
                    )
                )
            )
        )
    }
}

@ExcludeFromJacocoGeneratedReport
@ScreenPreview
@Composable
fun Preview() {
    SignOutErrorScreen { }
}
