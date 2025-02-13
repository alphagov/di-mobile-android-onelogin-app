package uk.gov.onelogin.mainnav.ui

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
import uk.gov.android.onelogin.R
import uk.gov.android.ui.theme.m3.GdsTheme
import uk.gov.onelogin.mainnav.nav.BottomNavDestination
import uk.gov.onelogin.ui.home.HomeScreen
import uk.gov.onelogin.ui.settings.SettingsScreen
import uk.gov.onelogin.wallet.WalletScreenViewModel
import uk.gov.ui.components.navigation.GdsNavigationBar
import uk.gov.ui.components.navigation.GdsNavigationItem

@Suppress("LongMethod")
@Composable
fun MainNavScreen(
    navController: NavHostController = rememberNavController(),
    walletScreenViewModel: WalletScreenViewModel = hiltViewModel()
) {
    val navItems = createBottomNavItems(walletScreenViewModel.walletEnabled)
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
                                Icon(painterResource(id = navDest.icon), navDest.key)
                            },
                            onClick = {
                                navController.navigate(navDest.key) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            selected = navBackStackEntry?.destination?.route == navDest.key,
                            label = {
                                Text(
                                    text = stringResource(id = navDest.label),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    fontSize = MaterialTheme
                                        .typography.bodyMedium.fontSize.nonScaledSp,
                                    fontWeight = FontWeight.Bold
                                )
                            },
                            colors = {
                                NavigationBarItemDefaults.colors(
                                    indicatorColor = colorResource(id = R.color.nav_bottom_selected)
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

private fun createBottomNavItems(walletEnabled: State<Boolean>) =
    if (walletEnabled.value) {
        listOf(
            BottomNavDestination.Home,
            BottomNavDestination.Wallet,
            BottomNavDestination.Settings
        )
    } else {
        listOf(
            BottomNavDestination.Home,
            BottomNavDestination.Settings
        )
    }

val TextUnit.nonScaledSp
    @Composable
    get() = (this.value / LocalDensity.current.fontScale).sp
