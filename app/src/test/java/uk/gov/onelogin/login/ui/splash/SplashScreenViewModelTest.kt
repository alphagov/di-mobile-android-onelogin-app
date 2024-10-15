package uk.gov.onelogin.login.ui.splash

import android.os.Handler
import android.os.Looper
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoInteractions
import org.mockito.kotlin.whenever
import uk.gov.onelogin.appinfo.service.domain.AppInfoService
import uk.gov.onelogin.appinfo.service.domain.model.AppInfoServiceState
import uk.gov.onelogin.extensions.CoroutinesTestExtension
import uk.gov.onelogin.extensions.InstantExecutorExtension
import uk.gov.onelogin.login.LoginRoutes
import uk.gov.onelogin.login.state.LocalAuthStatus
import uk.gov.onelogin.login.usecase.HandleLogin
import uk.gov.onelogin.mainnav.MainNavRoutes
import uk.gov.onelogin.navigation.Navigator
import uk.gov.onelogin.signOut.SignOutRoutes
import uk.gov.onelogin.ui.error.ErrorRoutes

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(InstantExecutorExtension::class, CoroutinesTestExtension::class)
class SplashScreenViewModelTest {
    private val mockHandleLogin: HandleLogin = mock()
    private val mockNavigator: Navigator = mock()
    private val mockLifeCycleOwner: LifecycleOwner = mock()
    private val mockActivity: FragmentActivity = mock()
    private val mockAppInfoService: AppInfoService = mock()

    private val viewModel = SplashScreenViewModel(
        mockNavigator,
        mockHandleLogin,
        mockAppInfoService
    )

    @Test
    fun loginFailsWithSecureStoreError() = runTest {
        whenever(mockHandleLogin.invoke(any(), any()))
            .thenAnswer {
                (it.arguments[1] as (LocalAuthStatus) -> Unit).invoke(
                    LocalAuthStatus.SecureStoreError
                )
            }
        viewModel.login(mockActivity)

        verify(mockNavigator).goBack()
        verify(mockNavigator).navigate(LoginRoutes.SignInError, false)
    }

    @Test
    fun loginFailsWithBioCheckFailed() = runTest {
        whenever(mockHandleLogin.invoke(any(), any()))
            .thenAnswer {
                (it.arguments[1] as (LocalAuthStatus) -> Unit).invoke(
                    LocalAuthStatus.BioCheckFailed
                )
            }
        viewModel.login(mockActivity)

        verifyNoInteractions(mockNavigator)
    }

    @Test
    fun loginSuccess() = runTest {
        whenever(mockHandleLogin.invoke(any(), any()))
            .thenAnswer {
                (it.arguments[1] as (LocalAuthStatus) -> Unit).invoke(
                    LocalAuthStatus.Success("token")
                )
            }
        viewModel.login(mockActivity)

        verify(mockNavigator).goBack()
        verify(mockNavigator).navigate(MainNavRoutes.Start, false)
    }

    @Test
    fun loginRequiresRefresh() = runTest {
        whenever(mockHandleLogin.invoke(any(), any()))
            .thenAnswer {
                (it.arguments[1] as (LocalAuthStatus) -> Unit).invoke(
                    LocalAuthStatus.ManualSignIn
                )
            }
        viewModel.login(mockActivity)

        verify(mockNavigator).goBack()
        verify(mockNavigator).navigate(LoginRoutes.Welcome, false)
    }

    @Test
    fun loginRequiresReAuth() = runTest {
        whenever(mockHandleLogin.invoke(any(), any()))
            .thenAnswer {
                (it.arguments[1] as (LocalAuthStatus) -> Unit).invoke(
                    LocalAuthStatus.ReAuthSignIn
                )
            }
        viewModel.login(mockActivity)

        verify(mockNavigator).goBack()
        verify(mockNavigator).navigate(SignOutRoutes.Info, false)
    }

    @Test
    fun loginReturnsUserCancelled() = runTest {
        whenever(mockHandleLogin.invoke(any(), any()))
            .thenAnswer {
                (it.arguments[1] as (LocalAuthStatus) -> Unit).invoke(
                    LocalAuthStatus.UserCancelled
                )
            }
        viewModel.login(mockActivity)

        verifyNoInteractions(mockNavigator)
        Handler(Looper.getMainLooper()).post {
            assertTrue(viewModel.showUnlock.value)
        }
    }

    @Test
    fun allowsSubsequentLoginCallsFromLockScreen() = runTest {
        // AND on resume called more than once
        viewModel.onResume(mockLifeCycleOwner)
        viewModel.onResume(mockLifeCycleOwner)

        // WHEN we call login
        viewModel.login(mockActivity)

        // THEN do NOT login (as the app will be going to background)
        verify(mockHandleLogin, times(1)).invoke(any(), any())
    }

    @Test
    fun retrieveAppInfoOffline() = runTest {
        // WHEN AppInfo call is offline and local source has failed/ null
        whenever(mockAppInfoService.get()).thenReturn(AppInfoServiceState.Offline)

        // AND it calls retrieveAppInfo
        viewModel.retrieveAppInfo()

        // THEN it navigates to Offline Error screen
        verify(mockNavigator).navigate(ErrorRoutes.Offline)
    }

    @Test
    fun retrieveAppInfoUnavailable() = runTest {
        // WHEN AppInfo call is offline and local source has failed/ null
        whenever(mockAppInfoService.get()).thenReturn(AppInfoServiceState.Unavailable)

        // AND it calls retrieveAppInfo
        viewModel.retrieveAppInfo()

        // THEN it navigates to Generic Error screen
        verify(mockNavigator).navigate(ErrorRoutes.Generic)
    }
}
