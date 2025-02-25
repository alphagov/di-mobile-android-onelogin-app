package uk.gov.onelogin.features

import uk.gov.onelogin.features.appinfo.data.model.AppInfoData

object TestUtils {
    val appInfoData =
        AppInfoData(
            apps =
            AppInfoData.App(
                AppInfoData.AppInfo(
                    minimumVersion = "1.0.0",
                    releaseFlags =
                    AppInfoData.ReleaseFlags(
                        true,
                        true,
                        true
                    ),
                    available = true,
                    featureFlags = AppInfoData.FeatureFlags(true)
                )
            )
        )

    val appInfoDataAppUnavailable =
        AppInfoData(
            apps =
            AppInfoData.App(
                AppInfoData.AppInfo(
                    minimumVersion = "1.0.0",
                    releaseFlags =
                    AppInfoData.ReleaseFlags(
                        true,
                        true,
                        true
                    ),
                    available = false,
                    featureFlags = AppInfoData.FeatureFlags(true)
                )
            )
        )

    val additionalAppInfoData =
        AppInfoData(
            apps =
            AppInfoData.App(
                AppInfoData.AppInfo(
                    minimumVersion = "1.0.0",
                    releaseFlags =
                    AppInfoData.ReleaseFlags(
                        true,
                        true,
                        true
                    ),
                    available = true,
                    featureFlags = AppInfoData.FeatureFlags(true)
                )
            )
        )

    val appInfoDataDisabledFeatures =
        AppInfoData(
            apps =
            AppInfoData.App(
                AppInfoData.AppInfo(
                    minimumVersion = "1.0.0",
                    releaseFlags =
                    AppInfoData.ReleaseFlags(
                        false,
                        false,
                        false
                    ),
                    available = true,
                    featureFlags = AppInfoData.FeatureFlags(false)
                )
            )
        )

    val updateRequiredAppInfoData =
        AppInfoData(
            apps =
            AppInfoData.App(
                AppInfoData.AppInfo(
                    minimumVersion = "2.0.0",
                    releaseFlags =
                    AppInfoData.ReleaseFlags(
                        true,
                        true,
                        true
                    ),
                    available = true,
                    featureFlags = AppInfoData.FeatureFlags(true)
                )
            )
        )

    val extractVersionErrorAppInfoData =
        AppInfoData(
            apps =
            AppInfoData.App(
                AppInfoData.AppInfo(
                    minimumVersion = "One.Two.Zero",
                    releaseFlags =
                    AppInfoData.ReleaseFlags(
                        true,
                        true,
                        true
                    ),
                    available = true,
                    featureFlags = AppInfoData.FeatureFlags(true)
                )
            )
        )
}
