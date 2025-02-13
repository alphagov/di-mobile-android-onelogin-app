package uk.gov.onelogin.ui.components

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import uk.gov.android.onelogin.R
import uk.gov.android.ui.theme.smallPadding

@Composable
fun EmailSection(email: String) {
    HorizontalDivider()
    Row(
        modifier = Modifier
            .padding(top = smallPadding)
            .background(color = MaterialTheme.colorScheme.inverseOnSurface)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Image(
            modifier = Modifier.padding(smallPadding),
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
                        fontSize = MaterialTheme.typography.bodySmall.fontSize
                    )
                ) {
                    append(email)
                }
            }
        )
    }
}

@Preview
@Composable
fun EmailSectionPreview() {
    EmailSection("mock@email.com")
}
