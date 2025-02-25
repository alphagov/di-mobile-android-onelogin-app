package uk.gov.onelogin.features.appinfo

import javax.inject.Inject

/**
 * Interface for utilities in [AppInfoService]
 */
fun interface AppInfoUtils {
    /**
     * Converts [String] version into [List] of [Int] to allow for comparison between versions.
     * @throws [AppError.IllegalVersionFormat] when version does not adhere to conventional commits versioning (e.g.: 1.2.0).
     */
    fun getComparableAppVersion(version: String): List<Int>

    sealed class AppError(msg: String) : Exception(msg) {
        data object IllegalVersionFormat : AppError(
            "Version number does not adhere to Major.minor.patch convention."
        )
    }

    companion object {
        // When app is released replace with: "https://play.google.com/store/apps/details?id=uk.gov.login"
        const val GOOGLE_PLAY_URL = "https://play.google.com/store/apps/"
    }
}

class AppInfoUtilsImpl @Inject constructor() : AppInfoUtils {
    override fun getComparableAppVersion(version: String): List<Int> {
        val cleanVersion = pattern.find(version)?.value
            ?: throw AppInfoUtils.AppError.IllegalVersionFormat

        val result = cleanVersion.split(".").map { it.toInt() }
        return result
    }

    companion object {
        private val pattern = "\\d+\\.\\d+\\.\\d+".toRegex()
    }
}
