package uk.gov.onelogin.features.appinfo.data

import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit
import javax.inject.Inject
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import uk.gov.onelogin.features.appinfo.data.model.AppInfoData
import uk.gov.onelogin.features.appinfo.data.model.AppInfoLocalState
import uk.gov.onelogin.features.appinfo.domain.AppInfoLocalSource
import uk.gov.onelogin.features.featureflags.domain.FeatureFlagSetter

class AppInfoLocalSourceImpl @Inject constructor(
    private val sharedPrefs: SharedPreferences,
    private val featureFlagSetter: FeatureFlagSetter
) : AppInfoLocalSource {
    override fun get(): AppInfoLocalState {
        return try {
            val result = sharedPrefs.getString(APP_INFO_KEY, null)
            if (!result.isNullOrEmpty()) {
                decodeAppInfoData(result)
            } else {
                AppInfoLocalState.Failure(APP_INFO_LOCAL_SOURCE_ERROR)
            }
        } catch (e: ClassCastException) {
            AppInfoLocalState.Failure(APP_INFO_CLASS_CAST_ERROR, e)
        }
    }

    override fun update(value: AppInfoData) {
        val encodedValue = Json.encodeToString<AppInfoData>(value)
        sharedPrefs.edit(true) {
            putString(APP_INFO_KEY, encodedValue)
        }
        featureFlagSetter.setFromAppInfo(value.apps.android)
    }

    private fun decodeAppInfoData(result: String): AppInfoLocalState {
        return try {
            val decodedResult = Json.decodeFromString<AppInfoData>(result)
            Log.d("AppInfoLocal", "$decodedResult")
            AppInfoLocalState.Success(decodedResult)
        } catch (e: SerializationException) {
            AppInfoLocalState.Failure(reason = APP_INFO_ILLEGAL_ARG_ERROR, exp = e)
        }
    }

    companion object {
        const val APP_INFO_KEY = "AppInfo.Key"
        const val APP_INFO_LOCAL_SOURCE_ERROR = "Retrieving AppInfo - does not exist"
        const val APP_INFO_ILLEGAL_ARG_ERROR = "Deserialization Error - invalid JSON"
        const val APP_INFO_CLASS_CAST_ERROR = "Retrieving AppInfo - Class Cast Exception"
    }
}
