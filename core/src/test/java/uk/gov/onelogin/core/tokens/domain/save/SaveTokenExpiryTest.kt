package uk.gov.onelogin.core.tokens.domain.save

import android.content.Context
import android.content.SharedPreferences
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import uk.gov.onelogin.core.tokens.utils.AuthTokenStoreKeys

class SaveTokenExpiryTest {
    private lateinit var useCase: SaveTokenExpiry

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

        useCase = SaveTokenExpiryImpl(mockContext)
    }

    @Test
    fun `check expiry saved`() {
        val expiry = 1L
        useCase(expiry)

        verify(mockEditor).putLong(AuthTokenStoreKeys.TOKEN_EXPIRY_KEY, expiry)
        verify(mockEditor).apply()
    }
}
