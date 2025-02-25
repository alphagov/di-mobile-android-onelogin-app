package uk.gov.onelogin.features.wallet.ui

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import uk.gov.android.featureflags.FeatureFlags
import uk.gov.android.wallet.sdk.WalletSdk
import uk.gov.onelogin.features.featureflags.data.WalletFeatureFlag

@HiltViewModel
class WalletScreenViewModel @Inject constructor(
    val walletSdk: WalletSdk,
    private val features: FeatureFlags
) : ViewModel() {
    private val _walletEnabled = mutableStateOf(false)
    val walletEnabled: State<Boolean> = _walletEnabled

    fun checkWalletEnabled() {
        _walletEnabled.value = features[WalletFeatureFlag.ENABLED]
    }
}
