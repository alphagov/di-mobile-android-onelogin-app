package uk.gov.onelogin.features.developer.ui.features

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import uk.gov.android.featureflags.FeatureFlag
import uk.gov.android.featureflags.FeatureFlags
import uk.gov.android.featureflags.InMemoryFeatureFlags
import uk.gov.onelogin.features.featureflags.data.AvailableFeatures

@HiltViewModel
class FeaturesScreenViewModel @Inject constructor(
    private val featureFlags: FeatureFlags,
    private val availableFeatures: AvailableFeatures
) : ViewModel() {
    private val _featureList = mutableStateOf(createMap())
    val featureList: State<Map<FeatureFlag, Boolean>>
        get() = _featureList

    fun toggleFeature(feature: FeatureFlag) {
        if (featureFlags[feature]) {
            (featureFlags as InMemoryFeatureFlags).minusAssign(setOf(feature))
        } else {
            (featureFlags as InMemoryFeatureFlags).plusAssign(setOf(feature))
        }

        _featureList.value = createMap()
    }

    private fun createMap(): Map<FeatureFlag, Boolean> {
        return availableFeatures.associateWith { featureFlags[it] }
    }
}
