package uk.gov.onelogin.features.wallet.ui

import kotlin.test.assertFalse
import kotlin.test.assertTrue
import org.junit.jupiter.api.Test
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import uk.gov.android.featureflags.FeatureFlags
import uk.gov.android.wallet.sdk.WalletSdk
import uk.gov.onelogin.features.featureflags.data.WalletFeatureFlag

class WalletScreenViewModelTest {
    private val walletSdk: WalletSdk = mock()
    private val featureFlags: FeatureFlags = mock()
    private val sut = WalletScreenViewModel(walletSdk, featureFlags)

    @Test
    fun `wallet visible`() {
        // Test initial state
        assertFalse(sut.walletEnabled.value)

        // WHEN
        whenever(featureFlags[eq(WalletFeatureFlag.ENABLED)]).thenReturn(true)
        sut.checkWalletEnabled()

        // THEN
        assertTrue(sut.walletEnabled.value)
    }

    @Test
    fun `wallet not visible`() {
        // Test initial state
        assertFalse(sut.walletEnabled.value)

        // WHEN
        whenever(featureFlags[eq(WalletFeatureFlag.ENABLED)]).thenReturn(false)
        sut.checkWalletEnabled()

        // THEN
        assertFalse(sut.walletEnabled.value)
    }
}
