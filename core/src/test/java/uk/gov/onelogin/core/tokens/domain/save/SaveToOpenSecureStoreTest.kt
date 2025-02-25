package uk.gov.onelogin.core.tokens.domain.save

import io.ktor.util.date.getTimeMillis
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
class SaveToOpenSecureStoreTest {
    private lateinit var useCase: SaveToOpenSecureStore
    private val mockSecureStore: SecureStore = mock()

    private val expectedStoreKey: String = "key"
    private val expectedStoreValueString: String = "value"
    private val expectedStoreValueNumber: Number = getTimeMillis()

    @BeforeEach
    fun setUp() {
        useCase = SaveToOpenSecureStoreImpl(mockSecureStore)
    }

    @Test
    fun `does not throw - when SecureStorageError thrown`() =
        runTest {
            whenever(mockSecureStore.upsert(any(), any())).thenThrow(
                SecureStorageError(Exception("Some error"))
            )

            assertDoesNotThrow {
                useCase.save(expectedStoreKey, expectedStoreValueString)
            }
        }

    @Test
    fun `saves value successfully (String, String)`() =
        runTest {
            useCase.save(expectedStoreKey, expectedStoreValueString)

            verify(mockSecureStore).upsert(
                expectedStoreKey,
                expectedStoreValueString
            )
        }

    @Test
    fun `saves value successfully (String, Number)`() =
        runTest {
            useCase.save(expectedStoreKey, expectedStoreValueNumber)

            verify(mockSecureStore).upsert(
                expectedStoreKey,
                expectedStoreValueNumber.toString()
            )
        }
}
