package uk.gov.onelogin.core.cleaner.domain

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.supervisorScope

interface ResultCollectionUtil {
    suspend fun <T> List<suspend () -> Result<T>>.runConcurrentlyForResults() = supervisorScope {
        map { async { it.invoke() } }.awaitAll()
    }

    fun List<Result<Unit>>.combineResults(): Result<Unit> = all { result -> result.isSuccess }
        .let { isSuccessful ->
            if (isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Failure(throwables))
            }
        }

    val <T> List<Result<T>>.throwables: List<Throwable>
        get() = filter { it.isFailure }.mapNotNull { it.exceptionOrNull() }

    class Failure(val causes: List<Throwable>) : Exception()
}
