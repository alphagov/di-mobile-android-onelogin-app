package uk.gov.onelogin.optin

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import uk.gov.onelogin.features.optin.data.AnalyticsOptInRepository
import uk.gov.onelogin.features.optin.data.OptInRepository
import uk.gov.onelogin.features.optin.domain.AnalyticsOptInLocalSource
import uk.gov.onelogin.features.optin.domain.FirebaseAnalyticsOptInSource
import uk.gov.onelogin.features.optin.domain.source.OptInLocalSource
import uk.gov.onelogin.features.optin.domain.source.OptInRemoteSource

@Module
@InstallIn(ViewModelComponent::class)
internal interface BinderModule {
    @Binds
    fun bindOptInLocalSource(source: AnalyticsOptInLocalSource): OptInLocalSource

    @Binds
    fun bindOptInRemoteSource(source: FirebaseAnalyticsOptInSource): OptInRemoteSource

    @Binds
    fun bindOptInRepository(repository: AnalyticsOptInRepository): OptInRepository
}
