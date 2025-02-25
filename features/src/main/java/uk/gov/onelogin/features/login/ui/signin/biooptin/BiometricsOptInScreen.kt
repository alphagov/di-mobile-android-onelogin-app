package uk.gov.onelogin.features.login.ui.signin.biooptin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import uk.gov.android.onelogin.core.R
import uk.gov.android.ui.components.GdsVectorImage
import uk.gov.android.ui.components.VectorImageParameters
import uk.gov.android.ui.components.content.GdsContentText
import uk.gov.android.ui.components.m3.Heading
import uk.gov.android.ui.components.m3.HeadingSize
import uk.gov.android.ui.components.m3.buttons.ButtonParameters
import uk.gov.android.ui.components.m3.buttons.ButtonType
import uk.gov.android.ui.components.m3.buttons.GdsButton
import uk.gov.android.ui.components.m3.content.ContentParameters
import uk.gov.android.ui.components.m3.content.GdsContent
import uk.gov.android.ui.theme.m3.GdsTheme
import uk.gov.android.ui.theme.smallPadding
import uk.gov.onelogin.core.ui.meta.ExcludeFromJacocoGeneratedReport
import uk.gov.onelogin.core.ui.meta.ScreenPreview

@Composable
fun BiometricsOptInScreen(
    viewModel: BioOptInViewModel = hiltViewModel(),
    analyticsViewModel: BioOptInAnalyticsViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) { analyticsViewModel.trackBioOptInScreen() }
    GdsTheme {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(smallPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1F),
                verticalArrangement = Arrangement.Center
            ) {
                Content()
            }
            BioOptInButtons(analyticsViewModel, viewModel)
        }
    }
}

@Composable
private fun Content() {
    GdsVectorImage(
        VectorImageParameters(
            image = R.drawable.bio_opt_in,
            scale = ContentScale.Fit
        )
    )
    Heading(
        modifier = Modifier.padding(top = smallPadding),
        text = R.string.app_enableBiometricsTitle,
        size = HeadingSize.DisplaySmall(),
        textAlign = TextAlign.Center
    ).generate()
    GdsContent(
        ContentParameters(
            resource = listOf(
                GdsContentText.GdsContentTextString(
                    intArrayOf(
                        R.string.app_enableBiometricsBody1
                    )
                )
            )
        )
    )
    GdsContent(
        ContentParameters(
            resource = listOf(
                GdsContentText.GdsContentTextString(
                    intArrayOf(R.string.app_enableBiometricsBody2)
                )
            )
        )
    )
    GdsContent(
        ContentParameters(
            resource = listOf(
                GdsContentText.GdsContentTextString(
                    intArrayOf(R.string.app_enableBiometricsBody3)
                )
            )
        )
    )
}

@Composable
private fun BioOptInButtons(
    analyticsViewModel: BioOptInAnalyticsViewModel,
    viewModel: BioOptInViewModel
) {
    Column(
        verticalArrangement = Arrangement.Bottom
    ) {
        GdsButton(
            ButtonParameters(
                modifier = Modifier.fillMaxWidth(),
                buttonType = ButtonType.PRIMARY(),
                text = stringResource(R.string.app_enableBiometricsButton),
                onClick = {
                    analyticsViewModel.trackBiometricsButton()
                    viewModel.useBiometrics()
                }
            )
        )
        GdsButton(
            ButtonParameters(
                modifier = Modifier.fillMaxWidth(),
                buttonType = ButtonType.QUATERNARY(),
                text = stringResource(R.string.app_enablePasscodeOrPatternButton),
                onClick = {
                    analyticsViewModel.trackPasscodeButton()
                    viewModel.doNotUseBiometrics()
                }
            )
        )
    }
}

@ExcludeFromJacocoGeneratedReport
@ScreenPreview
@Composable
internal fun BiometricsPreview() {
    BiometricsOptInScreen()
}
