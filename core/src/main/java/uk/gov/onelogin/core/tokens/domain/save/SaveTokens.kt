package uk.gov.onelogin.core.tokens.domain.save

fun interface SaveTokens {
    suspend operator fun invoke()
}
