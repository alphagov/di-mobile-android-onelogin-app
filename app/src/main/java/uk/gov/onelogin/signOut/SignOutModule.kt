package uk.gov.onelogin.signOut

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import uk.gov.onelogin.features.signout.domain.SignOutUseCase
import uk.gov.onelogin.features.signout.domain.SignOutUseCaseImpl

@SuppressWarnings("kotlin:S6517")
@InstallIn(ViewModelComponent::class)
@Module
interface SignOutModule {
    @Binds
    fun bindSignOut(useCase: SignOutUseCaseImpl): SignOutUseCase
}
