package uk.gov.onelogin.features.developer.ui.app

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import uk.gov.onelogin.features.appinfo.data.model.AppInfoData
import uk.gov.onelogin.features.appinfo.data.model.AppInfoLocalState
import uk.gov.onelogin.features.appinfo.domain.AppInfoLocalSource
import uk.gov.onelogin.features.appinfo.domain.BuildConfigVersion

@HiltViewModel
class AppTabScreenViewModel @Inject constructor(
    private val appInfoLocalSource: AppInfoLocalSource,
    @BuildConfigVersion
    val version: String
) : ViewModel() {
    val appInfo: MutableStateFlow<AppInfoData?> = MutableStateFlow(null)

    fun getAppInfo() =
        when (val appInfoStatus = appInfoLocalSource.get()) {
            is AppInfoLocalState.Failure -> appInfo.value = null
            is AppInfoLocalState.Success -> appInfo.value = appInfoStatus.value
        }

    fun updateAppInfoData(data: AppInfoData) {
        appInfoLocalSource.update(data)
    }
}
