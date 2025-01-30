package uk.gov.onelogin.wallet

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import uk.gov.android.network.client.GenericHttpClient
import uk.gov.android.onelogin.R
import uk.gov.android.wallet.core.deletedata.DeleteAllDataUseCase
import uk.gov.android.wallet.core.navigation.Navigator
import uk.gov.android.wallet.sdk.WalletSdk
import uk.gov.android.wallet.sdk.WalletSdkImpl

@InstallIn(SingletonComponent::class)
@Module
object WalletModule {
    @Provides
    fun provideWalletSdk(
        @ApplicationContext
        context: Context,
        navigator: Navigator,
        genericHttpClient: GenericHttpClient,
        deleteAllDataUseCase: DeleteAllDataUseCase
    ): WalletSdk {
        val config = WalletSdk.Configuration(
            clientId = context.resources.getString(R.string.stsClientId),
            authNetworkClient = genericHttpClient
        )
        val walletSdk = WalletSdkImpl(navigator, deleteAllDataUseCase)
        walletSdk.init(config)
        return walletSdk
    }

    @Provides
    fun provideDeleteWalletData(walletSdk: WalletSdk): DeleteWalletDataUseCase {
        return DeleteWalletDataUseCaseImpl(walletSdk)
    }
}
