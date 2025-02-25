package uk.gov.onelogin.features.appinfo.data

import kotlin.test.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever
import uk.gov.android.network.api.ApiResponse
import uk.gov.onelogin.features.appinfo.data.model.AppInfoData
import uk.gov.onelogin.features.appinfo.data.model.AppInfoRemoteState
import uk.gov.onelogin.features.appinfo.domain.AppInfoApi

class AppInfoRemoteSourceImplTest {
    private val appInfoApi: AppInfoApi = mock()
    private val data =
        AppInfoData(
            apps =
            AppInfoData.App(
                AppInfoData.AppInfo(
                    minimumVersion = "0.0.0",
                    releaseFlags =
                    AppInfoData.ReleaseFlags(
                        true,
                        true,
                        true
                    ),
                    available = true,
                    featureFlags = AppInfoData.FeatureFlags(true)
                )
            )
        )
    private val exp = Exception("Error")

    private val sut = AppInfoRemoteSourceImpl(appInfoApi)

    @Test
    fun `successful api call`() =
        runTest {
            whenever(appInfoApi.callApi()).thenReturn(ApiResponse.Success(data))
            val result = sut.get()
            assertEquals(AppInfoRemoteState.Success(data), result)
        }

    @Test
    fun `offline api call`() =
        runTest {
            whenever(appInfoApi.callApi()).thenReturn(ApiResponse.Offline)
            val result = sut.get()
            assertEquals(AppInfoRemoteState.Offline, result)
        }

    @Test
    fun `failed api call`() =
        runTest {
            whenever(appInfoApi.callApi()).thenReturn(ApiResponse.Failure(500, exp))
            val result = sut.get()
            assertEquals(
                AppInfoRemoteState.Failure(AppInfoRemoteSourceImpl.APP_INFO_REMOTE_SOURCE_ERROR),
                result
            )
        }
}
