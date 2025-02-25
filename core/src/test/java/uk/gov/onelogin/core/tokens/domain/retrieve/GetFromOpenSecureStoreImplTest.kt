package uk.gov.onelogin.core.tokens.domain.retrieve

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlinx.coroutines.test.runTest
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever
import uk.gov.android.securestore.RetrievalEvent
import uk.gov.android.securestore.SecureStore
import uk.gov.android.securestore.error.SecureStoreErrorType

class GetFromOpenSecureStoreImplTest {
    private val secureStore: SecureStore = mock()
    private val useCase = GetFromOpenSecureStoreImpl(secureStore)

    @Test
    fun verifySuccess() = runTest {
        val expected = mapOf("Key" to "Success")
        whenever(secureStore.retrieve(ArgumentMatchers.any()))
            .thenReturn(RetrievalEvent.Success(expected))

        val result = useCase.invoke("Key")
        assertEquals(expected, result)
    }

    @Test
    fun verifyFailure() = runTest {
        whenever(secureStore.retrieve(ArgumentMatchers.any()))
            .thenReturn(RetrievalEvent.Failed(SecureStoreErrorType.NOT_FOUND))

        val result = useCase.invoke("Key")
        assertNull(result)
    }
}
