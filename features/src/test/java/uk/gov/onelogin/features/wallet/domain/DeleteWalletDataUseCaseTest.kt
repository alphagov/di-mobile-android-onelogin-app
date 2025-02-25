package uk.gov.onelogin.features.wallet.domain

import androidx.fragment.app.FragmentActivity
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import uk.gov.android.wallet.sdk.WalletSdk
import uk.gov.android.wallet.sdk.WalletSdkImpl
import uk.gov.onelogin.features.wallet.domain.DeleteWalletDataUseCaseImpl.Companion.DELETE_WALLET_DATA_ERROR

class DeleteWalletDataUseCaseTest {
    private val walletSdk: WalletSdkImpl = mock()
    private val activityFragment: FragmentActivity = mock()
    private val sut = DeleteWalletDataUseCaseImpl(walletSdk)

    @Test
    fun `delete wallet data success`() =
        runBlocking {
            whenever(walletSdk.deleteWalletData(any())).thenReturn(true)
            val result = sut.invoke(activityFragment)
            assertEquals(Unit, result)
        }

    @Test
    fun `delete wallet data error`() =
        runBlocking {
            whenever(walletSdk.deleteWalletData(any())).thenReturn(false)
            val result =
                Assertions.assertThrows(
                    DeleteWalletDataUseCaseImpl.DeleteWalletDataError::class.java
                ) {
                    runBlocking { sut.invoke(activityFragment) }
                }
            assertTrue(result.message.contains(DELETE_WALLET_DATA_ERROR))
        }

    @Test
    fun `walled sdk init error`(): Unit =
        runBlocking {
            whenever(walletSdk.deleteWalletData(any())).then {
                throw WalletSdk.WalletSdkError.SdkNotInitialised
            }
            Assertions.assertThrows(
                WalletSdk.WalletSdkError.SdkNotInitialised::class.java
            ) {
                runBlocking { sut.invoke(activityFragment) }
            }
        }
}
