package uk.gov.onelogin.core.tokens.domain.remove

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import uk.gov.onelogin.core.tokens.utils.AuthTokenStoreKeys

class RemoveTokenExpiryTest {
    private lateinit var useCase: RemoveTokenExpiry

    private val mockContext: Context = mock()
    private val mockSharedPreferences: SharedPreferences = mock()
    private val mockEditor: SharedPreferences.Editor = mock()

    @BeforeEach
    fun setup() {
        whenever(
            mockContext.getSharedPreferences(
                eq(AuthTokenStoreKeys.TOKEN_SHARED_PREFS),
                eq(Context.MODE_PRIVATE)
            )
        )
            .thenReturn(mockSharedPreferences)
        whenever(mockSharedPreferences.edit()).thenReturn(mockEditor)

        useCase = RemoveTokenExpiryImpl(mockContext)
    }

    @Test
    fun `check expiry token removed`() =
        runTest {
            val result = useCase.clean()

            verify(mockEditor).remove(AuthTokenStoreKeys.TOKEN_EXPIRY_KEY)
            verify(mockEditor).commit()
            assertEquals(Result.success(Unit), result)
        }
}
