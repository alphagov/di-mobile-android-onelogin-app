package uk.gov.onelogin.login

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import uk.gov.onelogin.core.tokens.domain.VerifyIdToken
import uk.gov.onelogin.core.tokens.domain.VerifyIdTokenImpl
import uk.gov.onelogin.core.tokens.domain.save.SaveTokens
import uk.gov.onelogin.core.tokens.domain.save.SaveTokensImpl
import uk.gov.onelogin.features.login.domain.signin.locallogin.HandleLocalLogin
import uk.gov.onelogin.features.login.domain.signin.locallogin.HandleLocalLoginImpl
import uk.gov.onelogin.features.login.domain.signin.loginredirect.HandleLoginRedirect
import uk.gov.onelogin.features.login.domain.signin.loginredirect.HandleLoginRedirectImpl
import uk.gov.onelogin.features.login.domain.signin.remotelogin.HandleRemoteLogin
import uk.gov.onelogin.features.login.domain.signin.remotelogin.HandleRemoteLoginImpl

@InstallIn(ViewModelComponent::class)
@Module
interface LoginUseCaseModule {
    @Binds
    fun bindHandleLogin(useCase: HandleLocalLoginImpl): HandleLocalLogin

    @Binds
    fun bindVerifyIdToken(usecase: VerifyIdTokenImpl): VerifyIdToken

    @Binds
    fun bindSaveTokens(saveTokens: SaveTokensImpl): SaveTokens

    @Binds
    fun bindHandleRemoteLogin(handleLogin: HandleRemoteLoginImpl): HandleRemoteLogin

    @Binds
    fun bindHandleLoginRedirect(handleRedirect: HandleLoginRedirectImpl): HandleLoginRedirect
}
