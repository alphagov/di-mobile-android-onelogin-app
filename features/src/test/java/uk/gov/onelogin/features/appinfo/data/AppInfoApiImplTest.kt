package uk.gov.onelogin.features.appinfo.data

import android.content.Context
import kotlin.test.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import uk.gov.android.network.api.ApiRequest
import uk.gov.android.network.api.ApiResponse
import uk.gov.android.network.client.GenericHttpClient
import uk.gov.android.network.online.OnlineChecker
import uk.gov.onelogin.features.appinfo.data.model.AppInfoData
import uk.gov.onelogin.features.appinfo.domain.AppInfoApiImpl

class AppInfoApiImplTest {
    private val context: Context = mock()
    private val httpClient: GenericHttpClient = mock()
    private val onlineChecker: OnlineChecker = mock()
    private val apiResponse =
        ClassLoader
            .getSystemResource("api/appInfoResponseValue.json").readText()
    private val data =
        ApiResponse.Success(
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
        )
    private val exp = Exception("Error")
    private lateinit var request: ApiRequest

    private val sut =
        AppInfoApiImpl(
            context,
            httpClient,
            onlineChecker
        )

    @BeforeEach
    fun setup() {
        request =
            ApiRequest.Get(
                url = "https://mobile.build.account.gov.uk/appInfo",
                headers =
                listOf(
                    "Cache-Control" to "no-store",
                    "Content-Type" to "application/json",
                    "Strict-Transport-Security" to "max-age=31536000",
                    "X-Content-Type-Options" to "nosniff",
                    "X-Frame-Options" to "DENY"
                )
            )

        whenever(context.getString(any())).thenReturn("/appInfo")
        whenever(context.getString(any(), eq("/appInfo"))).thenAnswer {
            "https://mobile.build.account.gov.uk/appInfo"
        }
    }

    @Test
    fun `device is offline`(): Unit =
        runTest {
            whenever(onlineChecker.isOnline()).thenReturn(false)
            val result = sut.callApi()
            assertEquals(ApiResponse.Offline, result)
        }

    @Test
    fun `app info call successful`(): Unit =
        runTest {
            whenever(onlineChecker.isOnline()).thenReturn(true)
            whenever(httpClient.makeRequest(request)).thenReturn(ApiResponse.Success(apiResponse))
            val result = sut.callApi()
            assertEquals(data, result)
        }

    @Test
    fun `app info call fail`() =
        runTest {
            whenever(onlineChecker.isOnline()).thenReturn(true)
            whenever(httpClient.makeRequest(request)).thenReturn(ApiResponse.Failure(500, exp))
            val result = sut.callApi()
            assertEquals(ApiResponse.Failure(500, exp), result)
        }
}
