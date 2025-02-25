package uk.gov.onelogin.core.tokens.domain.retrieve

import android.content.Context
import android.content.SharedPreferences
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import uk.gov.onelogin.core.tokens.utils.AuthTokenStoreKeys

class GetTokenExpiryTest {
    private lateinit var useCase: GetTokenExpiry

    private val mockContext: Context = mock()
    private val mockSharedPreferences: SharedPreferences = mock()

    @BeforeEach
    fun setup() {
        whenever(
            mockContext.getSharedPreferences(
                eq(AuthTokenStoreKeys.TOKEN_SHARED_PREFS),
                eq(Context.MODE_PRIVATE)
            )
        ).thenReturn(mockSharedPreferences)

        useCase = GetTokenExpiryImpl(mockContext)
    }

    @Test
    fun `returns null when value does not exist`() {
        whenever(mockSharedPreferences.getLong(AuthTokenStoreKeys.TOKEN_EXPIRY_KEY, 0))
            .thenReturn(0)

        val result = useCase()

        assertEquals(null, result)
    }

    @Test
    fun `returns expiry value successfully`() {
        val expectedExpiryValue = 123L
        whenever(mockSharedPreferences.getLong(AuthTokenStoreKeys.TOKEN_EXPIRY_KEY, 0)).thenReturn(
            expectedExpiryValue
        )

        val result = useCase()

        assertEquals(expectedExpiryValue, result)
    }
}
