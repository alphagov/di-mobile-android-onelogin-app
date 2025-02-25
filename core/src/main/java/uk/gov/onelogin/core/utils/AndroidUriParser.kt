package uk.gov.onelogin.core.utils

import android.net.Uri
import javax.inject.Inject

class AndroidUriParser @Inject constructor() : UriParser {
    override fun parse(uri: String) = Uri.parse(uri)
}
