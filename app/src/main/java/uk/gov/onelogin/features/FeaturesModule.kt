package uk.gov.onelogin.features

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import uk.gov.android.features.FeatureFlags
import uk.gov.android.features.InMemoryFeatureFlags

@Module
@InstallIn(
    SingletonComponent::class
)
object FeaturesModule {
    @Provides
    @Singleton
    fun providesFeatureFlags(): FeatureFlags = InMemoryFeatureFlags(
        setOf(WalletFeatureFlag.ENABLED, CriCardFeatureFlag.ENABLED)
    )
}
