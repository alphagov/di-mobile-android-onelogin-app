package uk.gov.onelogin.mainnav.ui

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import uk.gov.android.onelogin.core.R
import uk.gov.logging.api.analytics.extensions.getEnglishString
import uk.gov.logging.api.analytics.logging.AnalyticsLogger
import uk.gov.logging.api.analytics.parameters.data.TaxonomyLevel2
import uk.gov.logging.api.analytics.parameters.data.TaxonomyLevel3
import uk.gov.logging.api.v3dot1.logger.logEventV3Dot1
import uk.gov.logging.api.v3dot1.model.RequiredParameters
import uk.gov.logging.api.v3dot1.model.TrackEvent

class MainNavAnalyticsViewModelTest {
    private lateinit var homeButton: String
    private lateinit var walletButton: String
    private lateinit var settingsButton: String
    private lateinit var analyticsLogger: AnalyticsLogger
    private lateinit var viewModel: MainNavAnalyticsViewModel

    @Before
    fun setup() {
        val context: Context = ApplicationProvider.getApplicationContext()
        homeButton = context.getEnglishString(R.string.app_home)
        walletButton = context.getEnglishString(R.string.app_wallet)
        settingsButton = context.getEnglishString(R.string.app_settingsTitle)
        analyticsLogger = mock()
        viewModel = MainNavAnalyticsViewModel(context, analyticsLogger)
    }

    @Test
    fun trackHomeTab() {
        val event = TrackEvent.Icon(
            text = homeButton,
            params = RequiredParameters(
                taxonomyLevel2 = TaxonomyLevel2.HOME,
                taxonomyLevel3 = TaxonomyLevel3.UNDEFINED
            )
        )

        viewModel.trackHomeTabButton()

        verify(analyticsLogger).logEventV3Dot1(event)
    }

    @Test
    fun trackWalletTab() {
        val event = TrackEvent.Icon(
            text = walletButton,
            params = RequiredParameters(
                taxonomyLevel2 = TaxonomyLevel2.WALLET,
                taxonomyLevel3 = TaxonomyLevel3.UNDEFINED
            )
        )

        viewModel.trackWalletTabButton()

        verify(analyticsLogger).logEventV3Dot1(event)
    }

    @Test
    fun trackSettingsTab() {
        val event = TrackEvent.Icon(
            text = settingsButton,
            params = RequiredParameters(
                taxonomyLevel2 = TaxonomyLevel2.SETTINGS,
                taxonomyLevel3 = TaxonomyLevel3.UNDEFINED
            )
        )

        viewModel.trackSettingsTabButton()

        verify(analyticsLogger).logEventV3Dot1(event)
    }
}
