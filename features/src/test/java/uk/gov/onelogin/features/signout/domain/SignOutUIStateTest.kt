package uk.gov.onelogin.features.signout.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import uk.gov.onelogin.features.optin.ui.OptInUIState

class SignOutUIStateTest {
    @Test
    fun wallet() {
        val state = SignOutUIState.Wallet
        assertEquals(expected = state, actual = SignOutUIState.valueOf("Wallet"))
    }

    @Test
    fun `no wallet`() {
        val state = SignOutUIState.NoWallet
        assertEquals(expected = state, actual = SignOutUIState.valueOf("NoWallet"))
    }

    @Test
    fun values() {
        val list = SignOutUIState.values()
        assertEquals(expected = 2, actual = list.size)
    }

    @Test
    fun entries() {
        val list = OptInUIState.entries
        assertEquals(expected = 2, actual = list.size)
    }
}
