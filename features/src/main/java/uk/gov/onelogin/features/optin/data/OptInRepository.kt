package uk.gov.onelogin.features.optin.data

import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import uk.gov.onelogin.core.cleaner.domain.Cleaner
import uk.gov.onelogin.features.optin.domain.source.OptInLocalSource
import uk.gov.onelogin.features.optin.domain.source.OptInRemoteSource

interface OptInRepository : Cleaner {
    fun isOptInPreferenceRequired(): Flow<Boolean>

    fun hasAnalyticsOptIn(): Flow<Boolean>

    suspend fun optIn()

    suspend fun optOut()

    suspend fun synchronise()

    suspend fun reset()
}

@Suppress("MemberVisibilityCanBePrivate")
class AnalyticsOptInRepository @Inject constructor(
    private val localSource: OptInLocalSource,
    private val remoteSource: OptInRemoteSource
) : OptInRepository {
    override fun isOptInPreferenceRequired(): Flow<Boolean> =
        flow {
            val state = fetchOptInState()
            emit(state.isUnset)
        }

    override fun hasAnalyticsOptIn(): Flow<Boolean> =
        flow {
            val state = fetchOptInState()
            emit(state == AnalyticsOptInState.Yes)
        }

    suspend fun fetchOptInState(): AnalyticsOptInState = localSource.getState()

    override suspend fun optIn() {
        updateOptInState(AnalyticsOptInState.Yes)
    }

    override suspend fun optOut() {
        updateOptInState(AnalyticsOptInState.No)
    }

    suspend fun updateOptInState(state: AnalyticsOptInState) {
        localSource.update(state)
        remoteSource.update(state)
    }

    override suspend fun reset() {
        updateOptInState(AnalyticsOptInState.None)
    }

    override suspend fun synchronise() {
        val state = fetchOptInState()
        remoteSource.update(state)
    }

    override suspend fun clean(): Result<Unit> {
        reset()
        return Result.success(Unit)
    }
}
