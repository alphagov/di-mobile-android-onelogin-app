package uk.gov.onelogin.core.tokens.domain.retrieve

fun interface GetFromOpenSecureStore {
    /**
     * Use case for getting data from an open secure store instance.
     *
     * @param key [String] value of key value pair/ pairs to retrieve
     * @return The saved [String]s correlating to each key requested are returned unless there is
     * an issue or one of the keys requested does not exist, in which case, null is returned
     */
    suspend operator fun invoke(vararg key: String): Map<String, String>?
}
