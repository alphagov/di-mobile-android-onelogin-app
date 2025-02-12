package uk.gov.onelogin.login.ui.biooptin

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.verify
import uk.gov.android.onelogin.R
import uk.gov.logging.api.analytics.extensions.getEnglishString
import uk.gov.logging.api.analytics.logging.AnalyticsLogger
import uk.gov.logging.api.analytics.parameters.data.TaxonomyLevel2
import uk.gov.logging.api.analytics.parameters.data.TaxonomyLevel3
import uk.gov.logging.api.v3dot1.logger.logEventV3Dot1
import uk.gov.logging.api.v3dot1.model.RequiredParameters
import uk.gov.logging.api.v3dot1.model.TrackEvent
import uk.gov.logging.api.v3dot1.model.ViewEvent

class BioOptInAnalyticsViewModelTest {
    private lateinit var name: String
    private lateinit var id: String
    private lateinit var passcodeBtn: String
    private lateinit var biometricsBtn: String
    private lateinit var requiredParameters: RequiredParameters
    private lateinit var logger: AnalyticsLogger
    private lateinit var viewModel: BioOptInAnalyticsViewModel

    @Before
    fun setup() {
        val context: Context = ApplicationProvider.getApplicationContext()
        logger = mock()
        requiredParameters = RequiredParameters(
            taxonomyLevel2 = TaxonomyLevel2.APP_SYSTEM,
            taxonomyLevel3 = TaxonomyLevel3.UNDEFINED
        )
        name = context.getEnglishString(R.string.app_enableBiometricsTitle)
        id = context.getEnglishString(R.string.bio_opt_in_screen_page_id)
        passcodeBtn = context.getEnglishString(R.string.app_enablePasscodeOrPatternButton)
        biometricsBtn = context.getEnglishString(R.string.app_enableBiometricsButton)
        viewModel = BioOptInAnalyticsViewModel(context, logger)
    }

    @Test
    fun trackBioOptInScreen() {
        requiredParameters = RequiredParameters(
            taxonomyLevel2 = TaxonomyLevel2.LOGIN,
            taxonomyLevel3 = TaxonomyLevel3.BIOMETRICS
        )
        val event = ViewEvent.Screen(
            name = name,
            id = id,
            params = requiredParameters
        )

        viewModel.trackBioOptInScreen()

        verify(logger).logEventV3Dot1(event)
    }

    @Test
    fun trackBiometricsButton() {
        val event = TrackEvent.Button(
            text = biometricsBtn,
            params = requiredParameters
        )

        viewModel.trackBiometricsButton()

        verify(logger).logEventV3Dot1(event)
    }

    @Test
    fun trackPasscodeButton() {
        val event = TrackEvent.Button(
            text = passcodeBtn,
            params = requiredParameters
        )

        viewModel.trackPasscodeButton()

        verify(logger).logEventV3Dot1(event)
    }
}
