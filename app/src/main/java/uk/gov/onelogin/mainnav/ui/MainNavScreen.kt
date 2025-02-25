package uk.gov.onelogin.mainnav.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import uk.gov.android.onelogin.core.R
import uk.gov.android.ui.theme.m3.GdsTheme
import uk.gov.onelogin.features.home.ui.HomeScreen
import uk.gov.onelogin.features.settings.ui.SettingsScreen
import uk.gov.onelogin.features.wallet.ui.WalletScreenViewModel
import uk.gov.ui.components.navigation.GdsNavigationBar
import uk.gov.ui.components.navigation.GdsNavigationItem

@Suppress("LongMethod")
@Composable
fun MainNavScreen(
    navController: NavHostController = rememberNavController(),
    walletScreenViewModel: WalletScreenViewModel = hiltViewModel(),
    analyticsViewModel: MainNavAnalyticsViewModel = hiltViewModel()
) {
    val navItems = createBottomNavItems(
        walletScreenViewModel.walletEnabled,
        { analyticsViewModel.trackHomeTabButton() },
        { analyticsViewModel.trackWalletTabButton() },
        { analyticsViewModel.trackSettingsTabButton() }
    )
    GdsTheme {
        LaunchedEffect(Unit) {
            walletScreenViewModel.checkWalletEnabled()
        }
        Scaffold(
            bottomBar = {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                GdsNavigationBar(
                    items = navItems.map { navDest ->
                        GdsNavigationItem(
                            icon = {
                                Icon(painterResource(id = navDest.first.icon), navDest.first.key)
                            },
                            onClick = {
                                navDest.second()
                                navController.navigate(navDest.first.key) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            selected = navBackStackEntry?.destination?.route == navDest.first.key,
                            label = { Label(navDest.first.label) },
                            colors = {
                                NavigationBarItemDefaults.colors(
                                    indicatorColor = if (isSystemInDarkTheme()) {
                                        MaterialTheme.colorScheme.tertiary
                                    } else {
                                        colorResource(id = R.color.nav_bottom_selected)
                                    }
                                )
                            }
                        )
                    },
                    tonalElevation = 0.dp,
                    containerColor = {
                        MaterialTheme.colorScheme.background
                    }
                ).generate()
            }
        ) { paddingValues ->
            NavHost(
                navController = navController,
                startDestination = BottomNavDestination.Home.key,
                modifier = Modifier.padding(paddingValues)
            ) {
                composable(BottomNavDestination.Home.key) {
                    HomeScreen()
                }
                composable(BottomNavDestination.Wallet.key) {
                    walletScreenViewModel.walletSdk.WalletApp(deeplink = "", adminEnabled = false)
                }
                composable(BottomNavDestination.Settings.key) {
                    SettingsScreen()
                }
            }
        }
    }
}

@Composable
private fun Label(text: Int) {
    Text(
        text = stringResource(id = text),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        fontSize = MaterialTheme
            .typography.bodyMedium.fontSize.nonScaledSp,
        fontWeight = FontWeight.Bold
    )
}

/**
 * Based on the WalletFeatureFlag, it returns a [Pair] of [BottomNavDestination] and function to
 * allow for displaying the correct tabs, but also log analytics based on what tab has been clicked.
 *
 * @param walletEnabled - Wallet feature flag value which controls if the Wallet tab is displayed
 * @param trackHome - GA4 `trackEventIcon` for Home tab button
 * @param trackWallet - - GA4 `trackEventIcon` for Wallet tab button
 * @param trackProfile - GA4 `trackEventIcon` for Profile tab button
 */
private fun createBottomNavItems(
    walletEnabled: State<Boolean>,
    trackHome: () -> Unit,
    trackWallet: () -> Unit,
    trackProfile: () -> Unit
): List<Pair<BottomNavDestination, () -> Unit>> {
    val home = BottomNavDestination.Home to trackHome
    val wallet = BottomNavDestination.Wallet to trackWallet
    val profile = BottomNavDestination.Settings to trackProfile
    return if (walletEnabled.value) {
        listOf(home, wallet, profile)
    } else {
        listOf(home, profile)
    }
}

val TextUnit.nonScaledSp
    @Composable
    get() = (this.value / LocalDensity.current.fontScale).sp
