package uk.gov.onelogin.utils

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uk.gov.onelogin.core.utils.LocaleUtils
import uk.gov.onelogin.core.utils.LocaleUtilsImpl

@Module
@InstallIn(SingletonComponent::class)
interface LocaleModule {
    @Binds
    fun bindLocaleUtils(utils: LocaleUtilsImpl): LocaleUtils
}
