package uk.gov.onelogin.ui.error.offline

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import uk.gov.android.onelogin.R
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

@Composable
@Preview
fun OfflineErrorScreen(onRetryClick: () -> Unit = { }) {
    GdsTheme {
        val analyticsViewModel: OfflineErrorAnalyticsViewModel = hiltViewModel()
        BackHandler { analyticsViewModel.trackBackButton() }
        LaunchedEffect(Unit) { analyticsViewModel.trackScreen() }
        ErrorPage(
            parameters = ErrorPageParameters(
                primaryButtonParameters = ButtonParameters(
                    buttonType = ButtonType.PRIMARY(),
                    onClick = {
                        analyticsViewModel.trackButton()
                        onRetryClick()
                    },
                    text = R.string.app_tryAgainButton
                ),
                informationParameters = InformationParameters(
                    contentParameters = ContentParameters(
                        resource = listOf(
                            GdsContentText.GdsContentTextString(
                                subTitle = R.string.app_networkErrorTitle,
                                text = intArrayOf(R.string.app_networkErrorBody)
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
