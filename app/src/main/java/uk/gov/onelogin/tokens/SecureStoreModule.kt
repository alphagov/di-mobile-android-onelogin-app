package uk.gov.onelogin.tokens

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton
import uk.gov.android.securestore.AccessControlLevel
import uk.gov.android.securestore.SecureStorageConfiguration
import uk.gov.android.securestore.SecureStore
import uk.gov.android.securestore.SharedPrefsStore
import uk.gov.onelogin.core.tokens.data.initialise.AutoInitialiseSecureStore
import uk.gov.onelogin.core.tokens.data.initialise.AutoInitialiseSecureStoreImpl
import uk.gov.onelogin.core.tokens.utils.AuthTokenStoreKeys

@Module
@InstallIn(SingletonComponent::class)
object SecureStoreSingletonModule {

    @Provides
    @Singleton
    @Named("Token")
    fun providesTokenSecureStore(): SecureStore = SharedPrefsStore()

    @Provides
    @Singleton
    @Named("Open")
    fun providesOpenSecureStore(
        @ApplicationContext
        context: Context
    ): SecureStore = SharedPrefsStore().also {
        val configuration = SecureStorageConfiguration(
            AuthTokenStoreKeys.OPEN_SECURE_STORE_ID,
            AccessControlLevel.OPEN
        )
        it.init(context, configuration)
    }
}

@Module
@InstallIn(ViewModelComponent::class)
object SecureStoreViewModelModule {
    @Provides
    fun providesAutoInitialiseSecureStore(
        autoInitialiseSecureStoreImpl: AutoInitialiseSecureStoreImpl
    ): AutoInitialiseSecureStore = autoInitialiseSecureStoreImpl
}
