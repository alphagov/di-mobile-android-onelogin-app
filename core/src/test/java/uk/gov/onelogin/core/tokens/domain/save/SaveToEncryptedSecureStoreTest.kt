package uk.gov.onelogin.core.tokens.domain.save

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import uk.gov.android.securestore.SecureStore
import uk.gov.android.securestore.error.SecureStorageError

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(uk.gov.onelogin.core.extensions.CoroutinesTestExtension::class)
class SaveToEncryptedSecureStoreTest {
    private lateinit var useCase: SaveToEncryptedSecureStore
    private val mockSecureStore: SecureStore = mock()

    private val expectedStoreKey: String = "key"
    private val expectedStoreValue: String = "value"

    @BeforeEach
    fun setUp() {
        useCase = SaveToEncryptedSecureStoreImpl(mockSecureStore)
    }

    @Test
    fun `does not throw - when SecureStorageError thrown`() =
        runTest {
            whenever(mockSecureStore.upsert(any(), any())).thenThrow(
                SecureStorageError(Exception("Some error"))
            )

            assertDoesNotThrow {
                useCase.invoke(expectedStoreKey, expectedStoreValue)
            }
        }

    @Test
    fun `saves value successfully`() =
        runTest {
            useCase.invoke(expectedStoreKey, expectedStoreValue)

            verify(mockSecureStore).upsert(
                expectedStoreKey,
                expectedStoreValue
            )
        }
}
