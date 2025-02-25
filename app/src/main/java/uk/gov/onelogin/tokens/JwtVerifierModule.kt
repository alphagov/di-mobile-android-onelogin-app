package uk.gov.onelogin.tokens

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import uk.gov.android.authentication.json.jwt.Jose4jJwtVerifier
import uk.gov.android.authentication.json.jwt.JwtVerifier

@Module
@InstallIn(ViewModelComponent::class)
object JwtVerifierModule {
    @Provides
    fun bindJwtVerifier(): JwtVerifier = Jose4jJwtVerifier()
}
