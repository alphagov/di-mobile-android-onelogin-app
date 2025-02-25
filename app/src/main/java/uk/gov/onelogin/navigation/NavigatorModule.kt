package uk.gov.onelogin.navigation

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import uk.gov.onelogin.core.navigation.domain.Navigator
import uk.gov.onelogin.features.navigation.domain.NavigatorImpl

@Module
@InstallIn(SingletonComponent::class)
interface NavigatorModule {
    @Binds
    @Singleton
    fun bindNavigator(navigator: NavigatorImpl): Navigator
}
