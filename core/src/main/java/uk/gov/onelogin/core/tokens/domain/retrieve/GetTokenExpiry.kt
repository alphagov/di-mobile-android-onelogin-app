package uk.gov.onelogin.core.tokens.domain.retrieve

fun interface GetTokenExpiry {
    /**
     * Use case to get the expiry time of the token in open shared preferences
     *
     * @return expiry Long type timestamp of expiry time
     */
    operator fun invoke(): Long?
}
