package uk.gov.onelogin.tokens

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uk.gov.onelogin.core.tokens.domain.IsAccessTokenExpired
import uk.gov.onelogin.core.tokens.domain.IsAccessTokenExpiredImpl
import uk.gov.onelogin.core.tokens.domain.retrieve.GetTokenExpiry
import uk.gov.onelogin.core.tokens.domain.retrieve.GetTokenExpiryImpl

@Module
@InstallIn(SingletonComponent::class)
interface SingletonTokenModule {
    @Binds
    fun bindGetTokenExpiry(
        getTokenExpiry: GetTokenExpiryImpl
    ): GetTokenExpiry

    @Binds
    fun bindIsAccessTokenExpired(isTokenExpired: IsAccessTokenExpiredImpl): IsAccessTokenExpired
}
