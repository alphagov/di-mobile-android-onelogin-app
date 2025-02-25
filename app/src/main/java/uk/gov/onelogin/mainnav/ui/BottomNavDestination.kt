package uk.gov.onelogin.mainnav.ui

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import uk.gov.android.onelogin.core.R

sealed class BottomNavDestination(
    val key: String,
    @StringRes
    val label: Int,
    @DrawableRes
    val icon: Int
) {
    data object Home : BottomNavDestination(
        key = "home",
        label = R.string.app_home,
        icon = R.drawable.outline_home_24
    )
    data object Wallet : BottomNavDestination(
        key = "wallet",
        label = R.string.app_wallet,
        icon = R.drawable.outline_article_24
    )
    data object Settings : BottomNavDestination(
        key = "settings",
        label = R.string.app_settingsTitle,
        icon = R.drawable.ic_settings
    )
}
