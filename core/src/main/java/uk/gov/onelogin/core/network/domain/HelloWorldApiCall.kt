package uk.gov.onelogin.core.network.domain

interface HelloWorldApiCall {
    suspend fun happyPath(): String

    suspend fun errorPath(): String
}
