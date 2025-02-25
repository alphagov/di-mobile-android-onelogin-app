package uk.gov.onelogin.features.login.ui.splash

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
import uk.gov.logging.api.v3dot1.model.ViewEvent
import uk.gov.onelogin.features.login.ui.signin.splash.SplashScreenAnalyticsViewModel

class SplashScreenAnalyticsViewModelTest {
    private lateinit var context: Context
    private lateinit var name: String
    private lateinit var id: String
    private lateinit var nameUnlock: String
    private lateinit var idUnlock: String
    private lateinit var unlockButton: String
    private lateinit var backButton: String
    private lateinit var logger: AnalyticsLogger
    private lateinit var requiredParameters: RequiredParameters
    private lateinit var viewModel: SplashScreenAnalyticsViewModel

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        logger = mock()
        name = context.getEnglishString(R.string.app_splashScreenAnalyticsScreenName)
        id = context.getEnglishString(R.string.splash_screen_page_id)
        nameUnlock = context.getEnglishString(R.string.app_splashScreenUnlockAnalyticsScreenName)
        idUnlock = context.getEnglishString(R.string.splash_unlock_screen_page_id)
        unlockButton = context.getEnglishString(R.string.app_unlockButton)
        backButton = context.getEnglishString(R.string.system_backButton)
        viewModel = SplashScreenAnalyticsViewModel(context, logger)
    }

    @Test
    fun trackSplashScreen() {
        requiredParameters =
            RequiredParameters(
                taxonomyLevel2 = TaxonomyLevel2.LOGIN,
                taxonomyLevel3 = TaxonomyLevel3.UNDEFINED
            )
        val event =
            ViewEvent.Screen(
                name = name,
                id = id,
                params = requiredParameters
            )

        viewModel.trackSplashScreen(context, isLocked = false)

        verify(logger).logEventV3Dot1(event)
    }

    @Test
    fun trackSplashUnlockScreen() {
        requiredParameters =
            RequiredParameters(
                taxonomyLevel2 = TaxonomyLevel2.LOGIN,
                taxonomyLevel3 = TaxonomyLevel3.UNLOCK
            )
        val event =
            ViewEvent.Screen(
                name = nameUnlock,
                id = idUnlock,
                params = requiredParameters
            )

        viewModel.trackSplashScreen(context, isLocked = true)

        verify(logger).logEventV3Dot1(event)
    }

    @Test
    fun trackUnlockButton() {
        requiredParameters =
            RequiredParameters(
                taxonomyLevel2 = TaxonomyLevel2.LOGIN,
                taxonomyLevel3 = TaxonomyLevel3.UNLOCK
            )
        val event =
            TrackEvent.Button(
                text = unlockButton,
                params = requiredParameters
            )

        viewModel.trackUnlockButton()

        verify(logger).logEventV3Dot1(event)
    }

    @Test
    fun trackBackButtonSplash() {
        requiredParameters =
            RequiredParameters(
                taxonomyLevel2 = TaxonomyLevel2.LOGIN,
                taxonomyLevel3 = TaxonomyLevel3.UNDEFINED
            )
        val event =
            TrackEvent.Icon(
                text = backButton,
                params = requiredParameters
            )

        viewModel.trackBackButton(context, false)

        verify(logger).logEventV3Dot1(event)
    }

    @Test
    fun trackBackButtonUnlock() {
        requiredParameters =
            RequiredParameters(
                taxonomyLevel2 = TaxonomyLevel2.LOGIN,
                taxonomyLevel3 = TaxonomyLevel3.UNLOCK
            )
        val event =
            TrackEvent.Icon(
                text = backButton,
                params = requiredParameters
            )

        viewModel.trackBackButton(context, true)

        verify(logger).logEventV3Dot1(event)
    }
}
