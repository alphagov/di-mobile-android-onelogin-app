package uk.gov.onelogin.features.navigation.domain

import android.util.Log
import androidx.navigation.NavHostController
import javax.inject.Inject
import uk.gov.onelogin.core.navigation.domain.NavRoute
import uk.gov.onelogin.core.navigation.domain.Navigator
import uk.gov.onelogin.core.navigation.domain.hasPreviousBackStack
import uk.gov.onelogin.developer.DeveloperRoutes.navigateToDeveloperPanel

class NavigatorImpl @Inject constructor() : Navigator {
    private var navController: NavHostController? = null
        get() {
            if (field == null) Log.w(this::class.simpleName, "Navigator not initialised")
            return field
        }

    override fun setController(navController: NavHostController) {
        this.navController = navController
    }

    override fun navigate(
        route: NavRoute,
        popUpToInclusive: Boolean
    ) {
        Log.d("Navigator", "Navigating to: ${route.getRoute()}")
        navController?.let { controller ->
            if (popUpToInclusive) {
                controller.navigate(route.getRoute()) {
                    popUpTo(controller.graph.id) {
                        inclusive = true
                    }
                }
            } else {
                controller.navigate(route.getRoute())
            }
        }
    }

    override fun goBack() {
        navController?.popBackStack()
    }

    override fun openDeveloperPanel() {
        navController?.navigateToDeveloperPanel()
    }

    override fun hasBackStack(): Boolean = navController?.hasPreviousBackStack() ?: false

    override fun reset() {
        navController = null
    }
}
