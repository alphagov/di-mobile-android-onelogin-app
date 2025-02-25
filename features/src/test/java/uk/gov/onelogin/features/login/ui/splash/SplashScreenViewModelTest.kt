package uk.gov.onelogin.features.login.ui.splash

import android.os.Handler
import android.os.Looper
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoInteractions
import org.mockito.kotlin.whenever
import uk.gov.onelogin.core.navigation.data.ErrorRoutes
import uk.gov.onelogin.core.navigation.data.LoginRoutes
import uk.gov.onelogin.core.navigation.data.MainNavRoutes
import uk.gov.onelogin.core.navigation.data.SignOutRoutes
import uk.gov.onelogin.core.navigation.domain.Navigator
import uk.gov.onelogin.core.tokens.data.LocalAuthStatus
import uk.gov.onelogin.features.appinfo.data.model.AppInfoServiceState
import uk.gov.onelogin.features.appinfo.domain.AppInfoService
import uk.gov.onelogin.features.extensions.CoroutinesTestExtension
import uk.gov.onelogin.features.extensions.InstantExecutorExtension
import uk.gov.onelogin.features.login.domain.signin.locallogin.HandleLocalLogin
import uk.gov.onelogin.features.login.ui.signin.splash.SplashScreenViewModel

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(InstantExecutorExtension::class, CoroutinesTestExtension::class)
class SplashScreenViewModelTest {
    private val mockHandleLocalLogin: HandleLocalLogin = mock()
    private val mockNavigator: Navigator = mock()
    private val mockLifeCycleOwner: LifecycleOwner = mock()
    private val mockActivity: FragmentActivity = mock()
    private val mockAppInfoService: AppInfoService = mock()

    private val data = uk.gov.onelogin.features.TestUtils.appInfoData

    private val viewModel =
        SplashScreenViewModel(
            mockNavigator,
            mockHandleLocalLogin,
            mockAppInfoService
        )

    @Test
    fun loginFailsWithSecureStoreError() =
        runTest {
            whenever(mockHandleLocalLogin.invoke(any(), any()))
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
    fun loginFailsWithBioCheckFailed() =
        runTest {
            whenever(mockHandleLocalLogin.invoke(any(), any()))
                .thenAnswer {
                    (it.arguments[1] as (LocalAuthStatus) -> Unit).invoke(
                        LocalAuthStatus.BioCheckFailed
                    )
                }
            viewModel.login(mockActivity)

            verifyNoInteractions(mockNavigator)
        }

    @Test
    fun loginSuccess() =
        runTest {
            whenever(mockHandleLocalLogin.invoke(any(), any()))
                .thenAnswer {
                    (it.arguments[1] as (LocalAuthStatus) -> Unit).invoke(
                        LocalAuthStatus.Success(mapOf("key" to "token"))
                    )
                }
            viewModel.login(mockActivity)

            verify(mockNavigator).goBack()
            verify(mockNavigator).navigate(MainNavRoutes.Start, false)
        }

    @Test
    fun loginRequiresRefresh() =
        runTest {
            whenever(mockHandleLocalLogin.invoke(any(), any()))
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
    fun loginRequiresReAuth() =
        runTest {
            whenever(mockHandleLocalLogin.invoke(any(), any()))
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
    fun loginReturnsUserCancelled() =
        runTest {
            whenever(mockHandleLocalLogin.invoke(any(), any()))
                .thenAnswer {
                    (it.arguments[1] as (LocalAuthStatus) -> Unit).invoke(
                        LocalAuthStatus.UserCancelled
                    )
                }
            viewModel.login(mockActivity)

            verifyNoInteractions(mockNavigator)
            Handler(Looper.getMainLooper()).post {
                assertTrue(viewModel.showUnlock.value)
                assertFalse(viewModel.loading.value)
            }
        }

    @Test
    fun allowsSubsequentLoginCallsFromLockScreen() =
        runTest {
            // AND on resume called more than once
            viewModel.onResume(mockLifeCycleOwner)
            viewModel.onResume(mockLifeCycleOwner)

            // WHEN we call login
            viewModel.login(mockActivity)

            // THEN do NOT login (as the app will be going to background)
            verify(mockHandleLocalLogin, times(1)).invoke(any(), any())
        }

    @Test
    fun retrieveAppInfoOffline() =
        runTest {
            // WHEN AppInfo has not been called yet - initial state
            // THEN loading progress indicator will be set to false
            assertFalse(viewModel.loading.value)
            // AND AppInfo call is offline and local source has failed/ null
            whenever(mockAppInfoService.get()).thenReturn(AppInfoServiceState.Offline)

            // AND it calls retrieveAppInfo
            viewModel.retrieveAppInfo {
                // Callback - Nothing to do
            }

            // THEN loading progress indicator will be set to true
            assertTrue(viewModel.loading.value)
            // THEN it navigates to Offline Error screen
            verify(mockNavigator).navigate(ErrorRoutes.Offline)
        }

    @Test
    fun retrieveAppInfoUnavailable() =
        runTest {
            // WHEN AppInfo has not been called yet - initial state
            // THEN loading progress indicator will be set to false
            assertFalse(viewModel.loading.value)
            // AND AppInfo call is offline and local source has failed/ null
            whenever(mockAppInfoService.get()).thenReturn(AppInfoServiceState.Unavailable)

            // AND it calls retrieveAppInfo
            viewModel.retrieveAppInfo {
                // Callback - Nothing to do
            }

            // THEN loading progress indicator will be set to true
            assertTrue(viewModel.loading.value)
            // THEN it navigates to Unavailable Error screen
            verify(mockNavigator).navigate(ErrorRoutes.Unavailable)
        }

    @Test
    fun retrieveAppInfoGoodLocal() =
        runTest {
            // WHEN AppInfo call is successful from local
            whenever(mockAppInfoService.get()).thenReturn(AppInfoServiceState.Successful(data))

            // AND it calls retrieveAppInfo
            viewModel.retrieveAppInfo({})

            // THEN it does not navigate and calls set feature flags
            verifyNoInteractions(mockNavigator)
        }

    @Test
    fun retrieveAppInfoGoodRemote() =
        runTest {
            // WHEN AppInfo call is successful from the remote
            whenever(mockAppInfoService.get()).thenReturn(AppInfoServiceState.Successful(data))

            // AND it calls retrieveAppInfo
            viewModel.retrieveAppInfo({})

            // THEN it does not navigate and calls set feature flags
            verifyNoInteractions(mockNavigator)
        }

    @Test
    fun retrieveAppInfoSuccessful() =
        runTest {
            // WHEN AppInfo has not been called yet - initial state
            // THEN loading progress indicator will be set to false
            assertFalse(viewModel.loading.value)

            // AND AppInfo call is successful remote
            whenever(mockAppInfoService.get()).thenReturn(AppInfoServiceState.Successful(data))
            // AND it calls retrieveAppInfo
            viewModel.retrieveAppInfo {
                // Callback - Nothing to do
            }

            // THEN loading progress indicator will be set to true
            assertTrue(viewModel.loading.value)
        }

    @Test
    fun retrieveAppInfoUpdateRequired() =
        runTest {
            // WHEN AppInfo has not been called yet - initial state
            // THEN loading progress indicator will be set to false
            assertFalse(viewModel.loading.value)

            // AND AppInfo call is successful remote
            whenever(mockAppInfoService.get()).thenReturn(AppInfoServiceState.UpdateRequired)
            // AND it calls retrieveAppInfo
            viewModel.retrieveAppInfo {
                // Callback - Nothing to do
            }

            // THEN loading progress indicator will be set to true
            assertTrue(viewModel.loading.value)
            verify(mockNavigator).navigate(ErrorRoutes.UpdateRequired)
        }
}
