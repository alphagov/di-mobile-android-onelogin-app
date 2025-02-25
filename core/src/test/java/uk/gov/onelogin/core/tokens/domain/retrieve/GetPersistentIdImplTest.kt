package uk.gov.onelogin.core.tokens.domain.retrieve

import kotlin.test.assertNull
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever
import uk.gov.onelogin.core.tokens.utils.AuthTokenStoreKeys

class GetPersistentIdImplTest {
    private val expectedPersistentId = "cc893ece-b6bd-444d-9bb4-dec6f5778e50"
    private val mockGetFromOpenSecureStore: GetFromOpenSecureStore = mock()

    private val sut = GetPersistentIdImpl(mockGetFromOpenSecureStore)

    @Test
    fun successScenario() =
        runTest {
            whenever(mockGetFromOpenSecureStore.invoke(ArgumentMatchers.any()))
                .thenReturn(mapOf(AuthTokenStoreKeys.PERSISTENT_ID_KEY to expectedPersistentId))

            val idResponse = sut.invoke()

            assertEquals(expectedPersistentId, idResponse)
        }

    @Test
    fun missingToken() =
        runTest {
            // Given token is null
            whenever(mockGetFromOpenSecureStore(AuthTokenStoreKeys.PERSISTENT_ID_KEY)).thenReturn(
                null
            )

            val idResponse = sut.invoke()
            assertNull(idResponse)
        }
}
