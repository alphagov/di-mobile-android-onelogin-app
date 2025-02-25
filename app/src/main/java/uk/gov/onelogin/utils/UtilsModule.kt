package uk.gov.onelogin.utils

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import uk.gov.onelogin.core.utils.AndroidUriParser
import uk.gov.onelogin.core.utils.UriParser

@Module
@InstallIn(ViewModelComponent::class)
interface UtilsModule {
    @Binds
    fun bindUriParser(parser: AndroidUriParser): UriParser
}
