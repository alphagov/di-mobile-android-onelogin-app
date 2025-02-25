package uk.gov.onelogin.core.tokens.domain.save

interface SaveToOpenSecureStore {
    /**
     * Use case for saving data into an open secure store instance
     *
     * @param key [String] value of key value pair to save
     * @param value [String] value of value in key value pair to save
     */
    suspend fun save(
        key: String,
        value: String
    )

    suspend fun save(
        key: String,
        value: Number
    )
}
