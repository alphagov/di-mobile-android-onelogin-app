package uk.gov.onelogin.core.tokens.domain.save

fun interface SaveToEncryptedSecureStore {
    /**
     * Use case for saving data into a secure store instance
     *
     * @param key [String] value of key value pair to save
     * @param value [String] value of value in key value pair to save
     */
    suspend operator fun invoke(
        key: String,
        value: String
    )
}
