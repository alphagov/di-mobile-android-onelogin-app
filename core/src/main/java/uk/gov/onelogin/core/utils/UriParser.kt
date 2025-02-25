package uk.gov.onelogin.core.utils

import android.net.Uri

fun interface UriParser {
    fun parse(uri: String): Uri
}
