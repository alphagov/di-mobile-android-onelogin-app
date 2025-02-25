package uk.gov.onelogin.features.optin.ui

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import uk.gov.onelogin.features.optin.data.OptInRepository

@HiltViewModel
class OptInRequirementViewModel @Inject constructor(
    repository: OptInRepository
) : ViewModel() {
    val isOptInRequired = repository.isOptInPreferenceRequired()
}
