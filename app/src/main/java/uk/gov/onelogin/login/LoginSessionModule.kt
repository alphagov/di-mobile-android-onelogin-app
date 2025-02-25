package uk.gov.onelogin.login

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import uk.gov.android.authentication.login.AppAuthSession
import uk.gov.android.authentication.login.LoginSession

@Module
@InstallIn(SingletonComponent::class)
object LoginSessionModule {
    @Provides
    fun providesLoginSession(
        @ApplicationContext
        context: Context
    ): LoginSession = AppAuthSession(context)
}
