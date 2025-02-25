package uk.gov.onelogin.features.appinfo.data

import android.content.SharedPreferences
import kotlin.test.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import uk.gov.onelogin.features.appinfo.data.AppInfoLocalSourceImpl.Companion.APP_INFO_CLASS_CAST_ERROR
import uk.gov.onelogin.features.appinfo.data.AppInfoLocalSourceImpl.Companion.APP_INFO_ILLEGAL_ARG_ERROR
import uk.gov.onelogin.features.appinfo.data.AppInfoLocalSourceImpl.Companion.APP_INFO_KEY
import uk.gov.onelogin.features.appinfo.data.AppInfoLocalSourceImpl.Companion.APP_INFO_LOCAL_SOURCE_ERROR
import uk.gov.onelogin.features.appinfo.data.model.AppInfoData
import uk.gov.onelogin.features.appinfo.data.model.AppInfoLocalState
import uk.gov.onelogin.features.appinfo.domain.AppInfoLocalSource
import uk.gov.onelogin.features.featureflags.domain.FeatureFlagSetter

@OptIn(ExperimentalCoroutinesApi::class)
class AppInfoLocalSourceImplTest {
    private val prefs: SharedPreferences = mock()
    private val mockFeatureFlagSetter: FeatureFlagSetter = mock()

    private val dispatcher = StandardTestDispatcher()
    private val editor: SharedPreferences.Editor = mock()
    private val encodedValue =
        ClassLoader
            .getSystemResource("api/appInfoResponseValue.json").readText()
            .replace(" ", "").replace("\n", "")
    private val encodedInvalidValue =
        ClassLoader
            .getSystemResource("api/appInfoResponseValueInvalid.json").readText()
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

    lateinit var sut: AppInfoLocalSource

    @BeforeEach
    fun setup() {
        sut = AppInfoLocalSourceImpl(prefs, mockFeatureFlagSetter)
        Dispatchers.setMain(dispatcher)
        whenever(prefs.edit()).thenReturn(editor)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `successful retrieval`() {
        whenever(prefs.getString(eq(APP_INFO_KEY), eq(null))).thenReturn(encodedValue)
        val result = sut.get()
        assertEquals(AppInfoLocalState.Success(data), result)
    }

    @Test
    fun `successful retrieval - AppInfo is null or empty`() {
        whenever(prefs.getString(eq(APP_INFO_KEY), any())).thenReturn(null)
        val result = sut.get()
        assertEquals(
            AppInfoLocalState.Failure(APP_INFO_LOCAL_SOURCE_ERROR),
            result
        )
    }

    @Test
    fun `successful retrieval - data retrieved can't be deserialized into AppInfoData`() {
        whenever(prefs.getString(eq(APP_INFO_KEY), eq(null))).thenReturn(encodedInvalidValue)
        val result = sut.get()
        assert(
            (result as AppInfoLocalState.Failure).reason.contains(APP_INFO_ILLEGAL_ARG_ERROR)
        )
    }

    @Test
    fun `failed retrieval`() {
        whenever(prefs.getString(eq(APP_INFO_KEY), eq(null))).thenThrow(ClassCastException("Error"))
        val result = sut.get()
        assert(
            (result as AppInfoLocalState.Failure).reason.contains(APP_INFO_CLASS_CAST_ERROR)
        )
    }

    @Test
    fun `successful update`() {
        sut.update(data)
        verify(prefs.edit()).putString(APP_INFO_KEY, encodedValue)
        verify(editor).commit()
        verify(mockFeatureFlagSetter).setFromAppInfo(data.apps.android)
    }
}
