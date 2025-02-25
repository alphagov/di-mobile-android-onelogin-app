package uk.gov.onelogin.core.navigation.domain

import androidx.navigation.NavHostController

interface Navigator {
    fun setController(navController: NavHostController)

    fun navigate(
        route: NavRoute,
        popUpToInclusive: Boolean = false
    )

    fun goBack()

    fun openDeveloperPanel()

    fun hasBackStack(): Boolean

    fun reset()
}
