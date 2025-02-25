package uk.gov.onelogin.features.wallet.domain

import androidx.fragment.app.FragmentActivity
import javax.inject.Inject
import uk.gov.android.wallet.sdk.WalletSdk

fun interface DeleteWalletDataUseCase {
    suspend fun invoke(activityFragment: FragmentActivity)
}

class DeleteWalletDataUseCaseImpl @Inject constructor(
    private val walletSdk: WalletSdk
) : DeleteWalletDataUseCase {
    override suspend fun invoke(activityFragment: FragmentActivity) {
        val deleteResult = walletSdk.deleteWalletData(activityFragment)
        if (!deleteResult) throw DeleteWalletDataError()
    }

    data class DeleteWalletDataError(
        override val message: String = DELETE_WALLET_DATA_ERROR
    ) : Exception()

    companion object {
        const val DELETE_WALLET_DATA_ERROR = "Failed deleting wallet data."
    }
}
