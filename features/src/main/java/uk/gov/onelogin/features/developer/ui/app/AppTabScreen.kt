package uk.gov.onelogin.features.developer.ui.app

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import uk.gov.android.onelogin.features.BuildConfig
import uk.gov.android.ui.theme.largePadding
import uk.gov.android.ui.theme.mediumPadding
import uk.gov.onelogin.features.appinfo.data.model.AppInfoData

@Composable
fun AppTabScreen(viewModel: AppTabScreenViewModel = hiltViewModel()) {
    val data = viewModel.appInfo.collectAsState()
    LaunchedEffect(viewModel.appInfo) {
        viewModel.getAppInfo()
    }
    Column(
        modifier = Modifier.fillMaxSize().padding(mediumPadding),
        verticalArrangement = Arrangement.Center
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    modifier = Modifier.padding(8.dp),
                    text = buildAnnotatedString {
                        append("App flavor: ")
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append(BuildConfig.FLAVOR)
                        }
                    }
                )
                Text(text = "Version Name: ${viewModel.version}")
            }
        }
        Text(
            modifier = Modifier.padding(top = largePadding),
            style = MaterialTheme.typography.titleMedium,
            text = "App Info",
            textAlign = TextAlign.Left
        )
        data.value?.let { androidInfoData ->
            AppInfoView(
                androidInfoData.apps.android,
                viewModel
            )
        }
    }
}

@Composable
private fun AppInfoView(
    appInfoData: AppInfoData.AppInfo,
    viewModel: AppTabScreenViewModel
) {
    var minimumVersion by remember { mutableStateOf(appInfoData.minimumVersion) }
    var appAvailable by remember { mutableStateOf(appInfoData.available) }
    var walletEnabled by remember { mutableStateOf(appInfoData.releaseFlags.walletVisibleToAll) }
    var appCheckEnabled by remember { mutableStateOf(appInfoData.featureFlags.appCheckEnabled) }
    OutlinedTextField(
        value = minimumVersion,
        onValueChange = { minimumVersion = it },
        label = { Text(text = "Minimum version") }
    )
    Text("App Available")
    Switch(
        checked = appAvailable,
        onCheckedChange = { appAvailable = it }
    )
    Text("Wallet Visibility")
    Switch(
        checked = walletEnabled,
        onCheckedChange = { walletEnabled = it }
    )
    Text("Feature Flag - App Check Enabled")
    Switch(
        checked = appCheckEnabled,
        onCheckedChange = { appCheckEnabled = it }
    )
    Button(
        onClick = {
            val updatedData =
                AppInfoData(
                    AppInfoData.App(
                        AppInfoData.AppInfo(
                            minimumVersion = minimumVersion,
                            releaseFlags =
                            AppInfoData.ReleaseFlags(
                                walletVisibleViaDeepLink = false,
                                walletVisibleIfExists = false,
                                walletVisibleToAll = walletEnabled
                            ),
                            available = appAvailable,
                            featureFlags =
                            AppInfoData.FeatureFlags(
                                appCheckEnabled = appCheckEnabled
                            )
                        )
                    )
                )
            Log.d("UpdatedAppInfo", "$updatedData")
            viewModel.updateAppInfoData(updatedData)
        }
    ) {
        Text("Update App Info Data - Local Source")
    }
}
