package uk.gov.onelogin.features.developer.ui.appintegrity

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.hilt.navigation.compose.hiltViewModel
import uk.gov.android.ui.theme.smallPadding

@Composable
fun AppIntegrityTabScreen(viewModel: AppIntegrityTabViewModel = hiltViewModel()) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
            .padding(smallPadding),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            modifier = Modifier.padding(smallPadding)
        ) {
            Text(
                text =
                buildAnnotatedString {
                    appendBold("App Check")
                }
            )
        }

        DataItem(
            action1 = { viewModel.resetAttestation() },
            action2 = { viewModel.setFakeAttestation() },
            result = viewModel.clientAttestation,
            buttonText1 = "Remove attestation",
            buttonText2 = "Set fake attestation"
        )

        DataItem(
            action1 = { viewModel.resetAttestationExpiry() },
            action2 = { viewModel.setFutureAttestationExpiry() },
            result = viewModel.clientAttestationExpiry,
            buttonText1 = "Reset attestation expiry time",
            buttonText2 = "Set expiry in future"
        )

        ActionItem(
            action = { viewModel.getToken() },
            result = viewModel.tokenResponse,
            buttonText = "Get Firebase Token"
        )

        ActionItem(
            action = { viewModel.makeNetworkCall() },
            result = viewModel.networkResponse,
            buttonText = "Make Mobile Backend Api Call"
        )

        ActionItem(
            action = { viewModel.getClientAttestation() },
            result = viewModel.clientAttestationResult,
            buttonText = "Get Client Attestation"
        )

        ActionItem(
            action = { viewModel.generatePoP() },
            result = viewModel.proofOfPossessionResult,
            buttonText = "Generate Proof Of Possession"
        )
    }
}

@Composable
private fun ActionItem(
    action: () -> Unit,
    result: MutableState<String>,
    buttonText: String
) {
    Row {
        Button(
            modifier = Modifier.padding(start = smallPadding),
            onClick = action
        ) {
            Text(text = buttonText)
        }
    }
    Row(modifier = Modifier.padding(all = smallPadding)) {
        Text(text = result.value)
    }
}

@Composable
private fun DataItem(
    action1: () -> Unit,
    action2: () -> Unit,
    result: MutableState<String>,
    buttonText1: String,
    buttonText2: String
) {
    Row {
        Button(
            modifier = Modifier.padding(start = smallPadding).weight(1F),
            onClick = action1
        ) {
            Text(text = buttonText1)
        }
        Button(
            modifier = Modifier.padding(start = smallPadding).weight(1F),
            onClick = action2
        ) {
            Text(text = buttonText2)
        }
    }
    Row(modifier = Modifier.padding(all = smallPadding)) {
        Text(text = result.value)
    }
}

@Composable
private fun AnnotatedString.Builder.appendBold(token: String) {
    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
        append(token)
    }
}
