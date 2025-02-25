package uk.gov.onelogin.utils

import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.test.ext.junit.rules.ActivityScenarioRule
import uk.gov.onelogin.MainActivity
import uk.gov.onelogin.features.appinfo.data.model.AppInfoData

object TestUtils {
    val appInfoData = AppInfoData(
        apps = AppInfoData.App(
            AppInfoData.AppInfo(
                minimumVersion = "1.0.0",
                releaseFlags = AppInfoData.ReleaseFlags(
                    walletVisibleViaDeepLink = true,
                    walletVisibleIfExists = true,
                    walletVisibleToAll = true
                ),
                available = true,
                featureFlags = AppInfoData.FeatureFlags(true)
            )
        )
    )

    fun AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>.setActivity(
        action: () -> Unit
    ) {
        this.activityRule.scenario.onActivity { action() }
    }

    fun AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>.back() {
        this.activityRule.scenario.onActivity { activity ->
            activity.onBackPressedDispatcher.onBackPressed()
        }
    }
}
