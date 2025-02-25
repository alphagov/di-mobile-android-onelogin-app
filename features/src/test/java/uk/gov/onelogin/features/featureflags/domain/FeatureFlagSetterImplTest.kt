package uk.gov.onelogin.features.featureflags.domain

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import uk.gov.android.featureflags.InMemoryFeatureFlags
import uk.gov.onelogin.features.TestUtils
import uk.gov.onelogin.features.featureflags.data.AppIntegrityFeatureFlag
import uk.gov.onelogin.features.featureflags.data.WalletFeatureFlag

class FeatureFlagSetterImplTest {
    private val featureFlags: InMemoryFeatureFlags = mock()

    private lateinit var setFeatureFlagsImpl: FeatureFlagSetterImpl

    @BeforeEach
    fun setUp() {
        setFeatureFlagsImpl = FeatureFlagSetterImpl(featureFlags)
    }

    @Test
    fun `fromAppInfo() - Success - AppCheckEnabled true - should add AppCheckFeatureFlag`() {
        // Given
        val data = TestUtils.appInfoData.apps.android

        // When
        setFeatureFlagsImpl.setFromAppInfo(data)

        // Then
        verify(featureFlags).plusAssign(setOf(AppIntegrityFeatureFlag.ENABLED))
        verify(featureFlags, times(0)).minusAssign(setOf(AppIntegrityFeatureFlag.ENABLED))
    }

    @Test
    fun `fromAppInfo() - Success - AppCheckEnabled false - should remove AppCheckFeatureFlag`() {
        // Given
        val data = TestUtils.appInfoDataDisabledFeatures.apps.android

        // When
        setFeatureFlagsImpl.setFromAppInfo(data)

        // Then
        verify(featureFlags, times(0)).plusAssign(setOf(AppIntegrityFeatureFlag.ENABLED))
        verify(featureFlags).minusAssign(setOf(AppIntegrityFeatureFlag.ENABLED))
    }

    @Test
    fun `fromAppInfo() - Success - WalletVisibleToAll true - should add WalletFeatureFlag`() {
        // Given
        val data = TestUtils.appInfoData.apps.android

        // When
        setFeatureFlagsImpl.setFromAppInfo(data)

        // Then
        verify(featureFlags).plusAssign(setOf(AppIntegrityFeatureFlag.ENABLED))
        verify(featureFlags, times(0)).minusAssign(setOf(AppIntegrityFeatureFlag.ENABLED))
    }

    @Test
    fun `fromAppInfo() - Success - WalletVisibleToAll false - should remove WalletFeatureFlag`() {
        // Given
        val data = TestUtils.appInfoDataDisabledFeatures.apps.android

        // When
        setFeatureFlagsImpl.setFromAppInfo(data)

        // Then
        verify(featureFlags, times(0)).plusAssign(setOf(AppIntegrityFeatureFlag.ENABLED))
        verify(featureFlags).minusAssign(setOf(AppIntegrityFeatureFlag.ENABLED))
        verify(featureFlags, times(0)).plusAssign(setOf(WalletFeatureFlag.ENABLED))
        verify(featureFlags).minusAssign(setOf(WalletFeatureFlag.ENABLED))
    }
}
