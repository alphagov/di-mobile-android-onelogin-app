package uk.gov.onelogin.network

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import uk.gov.onelogin.core.network.domain.HelloWorldApiCall
import uk.gov.onelogin.core.network.domain.HelloWorldApiCallImpl

@Module
@InstallIn(ViewModelComponent::class)
fun interface NetworkUsecaseModule {
    @Binds
    fun bindHelloWorldApiCall(impl: HelloWorldApiCallImpl): HelloWorldApiCall
}
