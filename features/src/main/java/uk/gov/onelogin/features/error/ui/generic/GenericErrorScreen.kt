package uk.gov.onelogin.features.error.ui.generic

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
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
import uk.gov.onelogin.core.ui.meta.ExcludeFromJacocoGeneratedReport
import uk.gov.onelogin.core.ui.meta.ScreenPreview

@Composable
fun GenericErrorScreen(
    analyticsViewModel: GenericErrorAnalyticsViewModel = hiltViewModel(),
    onClick: () -> Unit = { }
) {
    GdsTheme {
        BackHandler {
            analyticsViewModel.trackBackButton()
            onClick()
        }
        LaunchedEffect(Unit) { analyticsViewModel.trackScreen() }
        ErrorPage(
            parameters = ErrorPageParameters(
                primaryButtonParameters = ButtonParameters(
                    buttonType = ButtonType.PRIMARY(),
                    onClick = {
                        analyticsViewModel.trackButton()
                        onClick()
                    },
                    text = R.string.app_closeButton
                ),
                informationParameters = InformationParameters(
                    contentParameters = ContentParameters(
                        resource =
                        listOf(
                            GdsContentText.GdsContentTextString(
                                subTitle = R.string.app_somethingWentWrongErrorTitle,
                                text =
                                intArrayOf(
                                    R.string.app_somethingWentWrongErrorBody
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
internal fun GenericErrorPreview() {
    GenericErrorScreen()
}
