package uk.gov.onelogin.optin

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import uk.gov.onelogin.features.optin.ui.IODispatcherQualifier

@Module
@InstallIn(ViewModelComponent::class)
internal object ProviderModule {
    private const val SHARED_PREFS_KEY = "SharedPrefs.key"

    @Provides
    fun provideSharedPreferences(
        @ApplicationContext context: Context
    ): SharedPreferences = context.getSharedPreferences(SHARED_PREFS_KEY, Context.MODE_PRIVATE)

    @IODispatcherQualifier
    @Provides
    @Suppress("InjectDispatcher") // this is the injection
    fun provideDispatcher(): CoroutineDispatcher = Dispatchers.IO
}
