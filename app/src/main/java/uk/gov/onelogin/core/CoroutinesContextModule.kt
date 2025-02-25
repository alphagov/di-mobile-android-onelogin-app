package uk.gov.onelogin.core

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import uk.gov.onelogin.core.tokens.utils.DefaultDispatcher
import uk.gov.onelogin.core.tokens.utils.IoDispatcher
import uk.gov.onelogin.core.tokens.utils.MainDispatcher

@Module
@InstallIn(SingletonComponent::class)
object CoroutinesContextModule {
    @Provides
    @IoDispatcher
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @DefaultDispatcher
    fun provideDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default

    @Provides
    @MainDispatcher
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main
}
