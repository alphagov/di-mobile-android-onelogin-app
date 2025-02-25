package uk.gov.onelogin.core.tokens.domain.remove

import kotlin.test.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import uk.gov.android.securestore.SecureStore
import uk.gov.android.securestore.error.SecureStorageError
import uk.gov.onelogin.core.extensions.CoroutinesTestExtension
import uk.gov.onelogin.core.tokens.utils.AuthTokenStoreKeys

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(CoroutinesTestExtension::class)
class RemoveAllSecureStoreDataTest {
    private lateinit var useCase: RemoveAllSecureStoreData
    private val mockSecureStore: SecureStore = mock()

    @BeforeEach
    fun setUp() {
        useCase = RemoveAllSecureStoreDataImpl(mockSecureStore)
    }

    @Test
    fun `removes access token and id token`() =
        runTest {
            val result = useCase.clean()

            verify(mockSecureStore).delete(
                AuthTokenStoreKeys.ACCESS_TOKEN_KEY
            )
            verify(mockSecureStore).delete(
                AuthTokenStoreKeys.ID_TOKEN_KEY
            )
            assertEquals(Result.success(Unit), result)
        }

    @Test
    fun `secure store exception is propagated up`() =
        runTest {
            whenever(
                mockSecureStore.delete(
                    AuthTokenStoreKeys.ACCESS_TOKEN_KEY
                )
            ).thenThrow(SecureStorageError(Exception("something went wrong")))

            val result = useCase.clean()
            assertTrue(result.isFailure)
            assertTrue(result.exceptionOrNull() is SecureStorageError)
            assertTrue(result.exceptionOrNull()?.message!!.contains("something went wrong"))
        }
}
