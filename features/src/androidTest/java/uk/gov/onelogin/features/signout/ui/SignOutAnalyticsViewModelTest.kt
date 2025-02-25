package uk.gov.onelogin.features.signout.ui

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.verify
import uk.gov.android.onelogin.core.R
import uk.gov.logging.api.analytics.extensions.getEnglishString
import uk.gov.logging.api.analytics.logging.AnalyticsLogger
import uk.gov.logging.api.analytics.parameters.data.TaxonomyLevel2
import uk.gov.logging.api.analytics.parameters.data.TaxonomyLevel3
import uk.gov.logging.api.v3dot1.logger.logEventV3Dot1
import uk.gov.logging.api.v3dot1.model.RequiredParameters
import uk.gov.logging.api.v3dot1.model.TrackEvent
import uk.gov.onelogin.features.TestUtils
import uk.gov.onelogin.features.TestUtils.executeTrackEventTestCase
import uk.gov.onelogin.features.signout.domain.SignOutUIState

class SignOutAnalyticsViewModelTest {
    private lateinit var buttonText: String
    private lateinit var name: String
    private lateinit var walletId: String
    private lateinit var noWalletId: String
    private lateinit var logger: AnalyticsLogger
    private lateinit var requiredParameters: RequiredParameters
    private lateinit var viewModel: SignOutAnalyticsViewModel

    @Before
    fun setUp() {
        val context: Context = ApplicationProvider.getApplicationContext()
        logger = mock()
        requiredParameters = RequiredParameters(
            taxonomyLevel2 = TaxonomyLevel2.ACCOUNT,
            taxonomyLevel3 = TaxonomyLevel3.SIGN_OUT
        )
        buttonText = context.getEnglishString(R.string.app_signOutAndDeleteAppDataButton)
        name = context.getEnglishString(R.string.app_signOutConfirmationTitle)
        walletId = context.getEnglishString(R.string.sign_out_wallet_page_id)
        noWalletId = context.getEnglishString(R.string.sign_out_no_wallet_page_id)
        viewModel = SignOutAnalyticsViewModel(context, logger)
    }

    @Test
    fun trackSignOutLogsTrackPrimary() {
        // Given a TrackEvent.Link
        val event = TrackEvent.Button(
            text = buttonText,
            params = requiredParameters
        )
        // When tracking re-auth
        viewModel.trackPrimary()
        // Then log a TrackEvent to the AnalyticsLogger
        verify(logger).logEventV3Dot1(event)
    }

    @Test
    fun trackIcons() {
        listOf(
            TestUtils.TrackEventTestCase.Icon(
                trackFunction = {
                    viewModel.trackCloseIcon()
                },
                text = "back"
            ),
            TestUtils.TrackEventTestCase.Icon(
                trackFunction = {
                    viewModel.trackBackPressed()
                },
                text = "back - system"
            )
        ).forEach {
            val result = executeTrackEventTestCase(it, requiredParameters)

            verify(logger).logEventV3Dot1(result)
        }
    }

    @Test
    fun trackScreens() {
        listOf(
            TestUtils.TrackEventTestCase.Screen(
                trackFunction = {
                    viewModel.trackSignOutView(SignOutUIState.Wallet)
                },
                name = name,
                id = walletId
            ),
            TestUtils.TrackEventTestCase.Screen(
                trackFunction = {
                    viewModel.trackSignOutView(SignOutUIState.NoWallet)
                },
                name = name,
                id = noWalletId
            )
        ).forEach {
            val result = executeTrackEventTestCase(it, requiredParameters)

            verify(logger).logEventV3Dot1(result)
        }
    }
}
