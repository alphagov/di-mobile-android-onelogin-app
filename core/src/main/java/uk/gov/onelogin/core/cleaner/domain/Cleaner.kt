package uk.gov.onelogin.core.cleaner.domain

fun interface Cleaner {
    suspend fun clean(): Result<Unit>
}
