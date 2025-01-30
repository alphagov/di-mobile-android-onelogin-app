package uk.gov.onelogin.ui.home

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import uk.gov.android.onelogin.R
import uk.gov.android.ui.pages.TitledPage
import uk.gov.android.ui.pages.TitledPageParameters
import uk.gov.onelogin.criorchestrator.sdk.publicapi.rememberCriOrchestrator
import uk.gov.onelogin.developer.DeveloperTools
import uk.gov.onelogin.ui.components.EmailHeader

@Suppress("LongMethod")
@Composable
@Preview
fun HomeScreen(
    viewModel: HomeScreenViewModel = hiltViewModel()
) {
    val tokens = viewModel.getTokens()
    val email = viewModel.email
    val httpClient = viewModel.httpClient
    TitledPage(
        parameters = TitledPageParameters(
            R.string.app_homeTitle
        ) {
            val criOrchestratorComponent = rememberCriOrchestrator(httpClient)
            Log.d("CriIOrchestratorInstance", "$criOrchestratorComponent")
            EmailHeader(email)
            Text(
                text = "Access Token",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp)
            )
            HorizontalDivider(Modifier.padding(start = 16.dp))
            Text(
                tokens?.accessToken ?: "No access token set!",
                modifier = Modifier
                    .padding(16.dp)
                    .testTag("homeScreen-accessToken")
            )
            HorizontalDivider()
            Text(
                text = "ID Token",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp)
            )
            HorizontalDivider(Modifier.padding(start = 16.dp))
            Text(
                text = tokens?.idToken ?: "No id token set!",
                modifier = Modifier
                    .padding(16.dp)
                    .testTag("homeScreen-idToken")
            )
            HorizontalDivider()
            Text(
                text = "Refresh Token",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp)
            )
            HorizontalDivider(Modifier.padding(start = 16.dp))
            Text(
                text = tokens?.refreshToken ?: "No refresh token set!",
                modifier = Modifier
                    .padding(
                        all = 16.dp
                    )
                    .testTag("homeScreen-refreshToken")
            )
            HorizontalDivider()
            if (DeveloperTools.isDeveloperPanelEnabled()) {
                TextButton(
                    onClick = { viewModel.openDevPanel() }
                ) {
                    Text("Developer Panel")
                }
            }
        }
    )
}
