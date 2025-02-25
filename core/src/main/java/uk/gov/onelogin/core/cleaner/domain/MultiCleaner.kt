package uk.gov.onelogin.core.cleaner.domain

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MultiCleaner(
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
    vararg cleaner: Cleaner
) : Cleaner, ResultCollectionUtil {
    private val cleaners = cleaner.toList()

    override suspend fun clean(): Result<Unit> = withContext(dispatcher) {
        cleaners
            .map { it::clean }
            .runConcurrentlyForResults()
            .combineResults()
    }
}
