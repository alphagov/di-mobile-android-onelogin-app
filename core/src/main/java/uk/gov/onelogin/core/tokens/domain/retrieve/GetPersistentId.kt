package uk.gov.onelogin.core.tokens.domain.retrieve

fun interface GetPersistentId {
    /**
     * Use case to get the persistent id from the id token, failing that
     * the open secure store is checked
     *
     * @return persistent id as a string or null if it fails to retrieve it
     */
    suspend operator fun invoke(): String?
}
