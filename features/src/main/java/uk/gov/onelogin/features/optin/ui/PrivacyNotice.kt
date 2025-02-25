package uk.gov.onelogin.features.optin.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import uk.gov.android.onelogin.core.R
import uk.gov.android.ui.theme.m3.GdsTheme
import uk.gov.android.ui.theme.smallPadding
import uk.gov.android.ui.theme.textSizeBody
import uk.gov.onelogin.core.ui.components.TextWithLink
import uk.gov.onelogin.core.ui.meta.ExcludeFromJacocoGeneratedReport

private const val WHITE_SPACE = " "
private const val ICON_KEY = "link_out.key"
internal const val ICON_TAG = "link_out.tag"
internal const val NOTICE_TAG = "notice.tag"

@Composable
fun PrivacyNotice(
    modifier: Modifier,
    style: TextStyle = TextStyle.Default,
    privacyNoticeString: String? = null,
    privacyNoticeLink: String,
    onPrivacyNotice: () -> Unit
) {
    TextWithLink(
        modifier = modifier.then(
            Modifier
                .minimumInteractiveComponentSize()
                .semantics {
                    role = Role.Button
                    onClick {
                        onPrivacyNotice()
                        true
                    }
                }
                .focusable()
                .testTag(NOTICE_TAG)
        ),
        text = privacyNoticeString,
        linkText = buildAnnotatedString {
            privacyNoticeString?.let {
                append(WHITE_SPACE)
            }
            withStyle(SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                append(privacyNoticeLink)
            }
            append(WHITE_SPACE)
            appendInlineContent(ICON_KEY)
        },
        style = style,
        inlineContent = mapOf(
            ICON_KEY to InlineTextContent(
                Placeholder(textSizeBody, textSizeBody, PlaceholderVerticalAlign.Top)
            ) {
                LinkOut()
            }
        )
    ) {
        onPrivacyNotice()
    }
}

@Composable
private fun LinkOut() {
    Image(
        colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.primary),
        painter = painterResource(id = R.drawable.link_out),
        modifier = Modifier
            .fillMaxSize()
            .testTag(ICON_TAG),
        contentDescription = null
    )
}

@ExcludeFromJacocoGeneratedReport
@PreviewFontScale
@PreviewLightDark
@Composable
internal fun PrivacyNoticePreview() {
    GdsTheme {
        PrivacyNotice(
            Modifier.padding(smallPadding),
            privacyNoticeString =
            stringResource(
                R.string.app_settingsAnalyticsToggleFootnote
            ),
            privacyNoticeLink =
            stringResource(
                id = R.string.app_settingsAnalyticsToggleFootnoteLink
            )
        ) {}
    }
}
