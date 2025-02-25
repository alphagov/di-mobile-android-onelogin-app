package uk.gov.onelogin.core.tokens.domain.retrieve

fun interface GetEmail {
    /**
     * Use case to get the email from the id token
     *
     * @return email as a string or null if it fails to retrieve it
     */
    operator fun invoke(): String?
}
