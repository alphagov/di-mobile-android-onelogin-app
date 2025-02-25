package uk.gov.onelogin.features.error.ui.unavailable

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.navigation.compose.rememberNavController
import uk.gov.android.onelogin.core.R
import uk.gov.android.ui.components.R as Res
import uk.gov.android.ui.theme.m3.GdsTheme
import uk.gov.android.ui.theme.smallPadding
import uk.gov.android.ui.theme.spacingDouble
import uk.gov.onelogin.core.navigation.domain.closeApp
import uk.gov.onelogin.core.ui.meta.ExcludeFromJacocoGeneratedReport
import uk.gov.onelogin.core.ui.meta.ScreenPreview

internal const val ICON_TAG = "icon.tag"
private val iconSize = 100.dp
private val iconPadding = 1.dp

@Composable
fun AppUnavailableScreen(analyticsViewModel: AppUnavailableAnalyticsViewModel = hiltViewModel()) {
    val navController = rememberNavController()
    GdsTheme { AppUnavailableBody() }
    BackHandler {
        analyticsViewModel.trackBackButton()
        navController.closeApp()
    }
    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) { analyticsViewModel.trackUnavailableView() }
}

@Composable
internal fun AppUnavailableBody() {
    // Update typography references when UI is updated
    val color = colorScheme.contentColorFor(colorScheme.background)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(smallPadding),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(spacingDouble, Alignment.Top),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                modifier = Modifier
                    .padding(iconPadding)
                    .size(iconSize)
                    .testTag(ICON_TAG),
                painter = painterResource(Res.drawable.ic_error),
                contentDescription = null,
                tint = color
            )
            Text(
                color = color,
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics { heading() },
                style = MaterialTheme.typography.headlineLarge, // `displaySmall`
                text = stringResource(R.string.app_appUnavailableTitle),
                textAlign = TextAlign.Center
            )
            Text(
                color = color,
                modifier = Modifier
                    .fillMaxWidth(),
                style = MaterialTheme.typography.bodyLarge, // `bodySmall`
                text = stringResource(R.string.app_appUnavailableBody),
                textAlign = TextAlign.Center
            )
        }
    }
}

@ExcludeFromJacocoGeneratedReport
@ScreenPreview
@Composable
internal fun AppUnavailablePreview() {
    GdsTheme { AppUnavailableBody() }
}
