package uk.gov.onelogin.features.error.ui.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import uk.gov.android.onelogin.core.R
import uk.gov.android.ui.components.R as UiR
import uk.gov.android.ui.components.content.GdsContentText
import uk.gov.android.ui.components.images.icon.IconParameters
import uk.gov.android.ui.components.m3.BulletListParameters
import uk.gov.android.ui.components.m3.GdsBulletList
import uk.gov.android.ui.components.m3.Heading
import uk.gov.android.ui.components.m3.HeadingSize
import uk.gov.android.ui.components.m3.buttons.ButtonParameters
import uk.gov.android.ui.components.m3.buttons.GdsButton
import uk.gov.android.ui.components.m3.content.ContentParameters
import uk.gov.android.ui.components.m3.content.GdsContent
import uk.gov.android.ui.components.m3.images.icon.GdsIcon
import uk.gov.android.ui.theme.m3.GdsTheme
import uk.gov.android.ui.theme.smallPadding
import uk.gov.android.wallet.core.ui.theme.buttonHeight
import uk.gov.onelogin.features.error.ui.auth.ErrorInformation.Companion.bulletPointIndentation
import uk.gov.onelogin.features.error.ui.auth.ErrorInformation.Companion.bulletPointTextPadding
import uk.gov.onelogin.features.error.ui.auth.ErrorInformation.Companion.listContentPadding

@Composable
fun AuthErrorScreen(viewModel: AuthErrorViewModel = hiltViewModel()) {
    GdsTheme {
        ConstraintLayout(
            modifier = Modifier
                .padding(horizontal = smallPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            val (content, button) = createRefs()
            Content(
                parameters = ErrorInformation(
                    title = R.string.app_dataDeletedErrorTitle,
                    intro = R.string.app_dataDeletedErrorBody1,
                    content = R.string.app_dataDeletedErrorBody2,
                    bulletList = listOf(
                        R.string.app_dataDeletedErrorBullet1,
                        R.string.app_dataDeletedErrorBullet2,
                        R.string.app_dataDeletedErrorBullet3
                    ),
                    instruction = R.string.app_dataDeletedErrorBody3
                ),
                modifier = Modifier
                    .constrainAs(content) {
                        top.linkTo(parent.top)
                        bottom.linkTo(button.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            )
            GdsButton(
                buttonParameters = ButtonParameters(
                    buttonType = uk.gov.android.ui.components.m3.buttons.ButtonType.PRIMARY(),
                    onClick = { viewModel.navigateToSignIn() },
                    text = stringResource(R.string.app_SignInWithGovUKOneLoginButton),
                    modifier = Modifier
                        .padding(bottom = smallPadding)
                        .fillMaxWidth()
                        .height(buttonHeight)
                        .constrainAs(button) {
                            bottom.linkTo(parent.bottom)
                        }
                )
            )
        }
    }
}

@Composable
private fun Content(
    parameters: ErrorInformation,
    modifier: Modifier
) {
    val iconContentDescription =
        stringResource(
            id = R.string.app_dataDeletedError_ContentDescription
        )
    parameters.apply {
        Column(
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
        ) {
            GdsIcon(
                parameters = IconParameters(
                    image = UiR.drawable.ic_error,
                    backGroundColor = MaterialTheme.colorScheme.background,
                    foreGroundColor = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .padding(bottom = smallPadding)
                        .semantics { contentDescription = iconContentDescription }
                )
            )
            Introduction()
            ContentList()
            GdsContent(
                contentParameters = ContentParameters(
                    resource = listOf(
                        GdsContentText.GdsContentTextString(
                            intArrayOf(instruction)
                        )
                    ),
                    textAlign = TextAlign.Center,
                    textPadding = PaddingValues(top = smallPadding)
                )
            )
        }
    }
}

@Composable
private fun ErrorInformation.ContentList() {
    GdsContent(
        contentParameters =
        ContentParameters(
            resource = listOf(
                GdsContentText.GdsContentTextString(
                    intArrayOf(content)
                )
            ),
            textAlign = TextAlign.Start,
            internalColumnModifier = Modifier.padding(bottom = listContentPadding)
        )
    )
    GdsBulletList(
        bulletListParameters = BulletListParameters(
            indent = bulletPointIndentation,
            textModifier = Modifier.padding(start = bulletPointTextPadding),
            contentText = GdsContentText.GdsContentTextString(
                text = bulletList.toIntArray()
            )
        )
    )
}

@Composable
private fun ErrorInformation.Introduction() {
    Heading(
        text = title,
        size = HeadingSize.DisplaySmall(),
        textAlign = TextAlign.Center,
        padding = PaddingValues(bottom = smallPadding)
    ).generate()
    GdsContent(
        contentParameters = ContentParameters(
            resource = listOf(
                GdsContentText.GdsContentTextString(
                    intArrayOf(intro)
                )
            ),
            textAlign = TextAlign.Center
        )
    )
}

data class ErrorInformation(
    val title: Int,
    val intro: Int,
    val content: Int,
    val bulletList: List<Int>,
    val instruction: Int
) {
    companion object {
        val listContentPadding = 12.dp
        val bulletPointIndentation = 10.dp
        val bulletPointTextPadding = 20.dp
    }
}
