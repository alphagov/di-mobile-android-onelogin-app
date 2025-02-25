package uk.gov.onelogin.core.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import uk.gov.android.onelogin.core.R
import uk.gov.android.ui.theme.smallPadding
import uk.gov.onelogin.core.ui.meta.ExcludeFromJacocoGeneratedReport
import uk.gov.onelogin.core.ui.meta.ScreenPreview

@Composable
fun EmailSection(email: String) {
    HorizontalDivider(Modifier.testTag(DIVIDER_TEST_TAG))
    Row(
        modifier = Modifier
            .padding(top = smallPadding)
            .background(color = MaterialTheme.colorScheme.inverseOnSurface)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Image(
            modifier = Modifier.padding(smallPadding)
                .testTag(IMAGE_TEST_TAG),
            painter = painterResource(id = R.drawable.ic_profile),
            contentDescription = null
        )
        Text(
            style = MaterialTheme.typography.bodyMedium,
            text = buildAnnotatedString {
                append(LocalContext.current.getText(R.string.app_settingsSignInDetailsTile))
                appendLine()
                withStyle(
                    style = SpanStyle(
                        fontSize = MaterialTheme.typography.bodySmall.fontSize,
                        color = MaterialTheme.colorScheme.surface
                    )
                ) {
                    append(email)
                }
            }
        )
    }
}

@ExcludeFromJacocoGeneratedReport
@ScreenPreview
@Composable
internal fun EmailSectionPreview() {
    EmailSection("mock@email.com")
}

internal const val DIVIDER_TEST_TAG = "divider_test_tag"
internal const val IMAGE_TEST_TAG = "image_test_tag"
