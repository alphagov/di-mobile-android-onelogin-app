package uk.gov.onelogin.features.developer.ui.tokens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uk.gov.onelogin.core.tokens.domain.retrieve.GetPersistentId
import uk.gov.onelogin.core.tokens.domain.save.SaveToOpenSecureStore
import uk.gov.onelogin.core.tokens.domain.save.SaveTokenExpiry
import uk.gov.onelogin.core.tokens.utils.AuthTokenStoreKeys

@HiltViewModel
class TokenTabScreenViewModel @Inject constructor(
    private val saveTokenExpiry: SaveTokenExpiry,
    private val saveToOpenSecureStore: SaveToOpenSecureStore,
    private val getPersistentId: GetPersistentId
) : ViewModel() {
    private val _persistentId = MutableStateFlow("")
    val persistentId: StateFlow<String>
        get() = _persistentId.asStateFlow()

    init {
        viewModelScope.launch {
            setPersistentId()
        }
    }

    fun resetAccessToken() {
        saveTokenExpiry(System.currentTimeMillis() - 1)
    }

    fun resetPersistentId() {
        viewModelScope.launch {
            saveToOpenSecureStore.save(
                AuthTokenStoreKeys.PERSISTENT_ID_KEY,
                ""
            )
            setPersistentId()
        }
    }

    private suspend fun setPersistentId() {
        _persistentId.value = getPersistentId() ?: ""
    }
}
