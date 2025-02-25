package uk.gov.onelogin.appinfo

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import uk.gov.android.onelogin.BuildConfig
import uk.gov.onelogin.features.appinfo.AppInfoUtils
import uk.gov.onelogin.features.appinfo.AppInfoUtilsImpl
import uk.gov.onelogin.features.appinfo.data.AppInfoLocalSourceImpl
import uk.gov.onelogin.features.appinfo.data.AppInfoRemoteSourceImpl
import uk.gov.onelogin.features.appinfo.data.AppVersionCheckImpl
import uk.gov.onelogin.features.appinfo.domain.AppInfoApi
import uk.gov.onelogin.features.appinfo.domain.AppInfoApiImpl
import uk.gov.onelogin.features.appinfo.domain.AppInfoLocalSource
import uk.gov.onelogin.features.appinfo.domain.AppInfoRemoteSource
import uk.gov.onelogin.features.appinfo.domain.AppInfoService
import uk.gov.onelogin.features.appinfo.domain.AppInfoServiceImpl
import uk.gov.onelogin.features.appinfo.domain.AppVersionCheck
import uk.gov.onelogin.features.appinfo.domain.BuildConfigVersion

@Module
@InstallIn(ViewModelComponent::class)
interface AppInfoApiModule {
    @Binds
    fun provideAppInfoApiCall(appInfoApiImpl: AppInfoApiImpl): AppInfoApi

    @Binds
    fun provideAppInfoRemoteSource(
        appInfoRemoteSource: AppInfoRemoteSourceImpl
    ): AppInfoRemoteSource

    @Binds
    fun provideAppInfoLocalSource(appInfoLocalSource: AppInfoLocalSourceImpl): AppInfoLocalSource

    @Binds
    fun provideAppInfoService(appInfoService: AppInfoServiceImpl): AppInfoService

    @Binds
    fun provideUtils(appInfoUtils: AppInfoUtilsImpl): AppInfoUtils

    @Binds
    fun provideAppVersionCheck(appVersionCheck: AppVersionCheckImpl): AppVersionCheck
}

@Module
@InstallIn(ViewModelComponent::class)
object BuildConfigProviderModule {
    @Provides
    @ViewModelScoped
    @BuildConfigVersion
    fun provideBuildConfigVersion(): String {
        return BuildConfig.VERSION_NAME
    }
}
