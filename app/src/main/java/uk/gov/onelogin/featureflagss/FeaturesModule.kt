package uk.gov.onelogin.featureflagss

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import uk.gov.android.featureflags.FeatureFlags
import uk.gov.android.featureflags.InMemoryFeatureFlags
import uk.gov.onelogin.features.featureflags.data.AvailableFeatures
import uk.gov.onelogin.features.featureflags.data.WalletFeatureFlag
import uk.gov.onelogin.features.featureflags.domain.FeatureFlagSetter
import uk.gov.onelogin.features.featureflags.domain.FeatureFlagSetterImpl

@Module
@InstallIn(
    SingletonComponent::class
)
object FeaturesModule {
    @Provides
    @Singleton
    fun providesFeatureFlags(): FeatureFlags = InMemoryFeatureFlags(
        setOf(WalletFeatureFlag.ENABLED)
    )

    @Provides
    @Singleton
    fun provideFeatureFlagSetter(
        featureFlags: FeatureFlags
    ): FeatureFlagSetter = FeatureFlagSetterImpl(featureFlags)

    @Provides
    @Singleton
    fun providesAvailableFeatures(): AvailableFeatures = AvailableFeatures()
}
