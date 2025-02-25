package uk.gov.onelogin.core.tokens.domain.save

fun interface SaveTokenExpiry {
    /**
     * Use case to save the expiry time of the token in open shared preferences
     *
     * @param expiry Long type timestamp of expiry time
     */
    operator fun invoke(expiry: Long)
}
