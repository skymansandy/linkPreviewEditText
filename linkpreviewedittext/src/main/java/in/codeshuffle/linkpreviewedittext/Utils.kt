package `in`.codeshuffle.linkpreviewedittext

import android.webkit.URLUtil
import java.net.URI
import java.net.URISyntaxException
import java.util.regex.Pattern

internal object Utils {

    private const val urlRegEx = ("(?:^|[\\W])((ht|f)tp(s?):\\/\\/|www\\.)"
            + "(([\\w\\-]+\\.){1,}?([\\w\\-.~]+\\/?)*"
            + "[\\p{Alnum}.,%_=?&#\\-+()\\[\\]\\*$~@!:/{};']*)")

    val urlPattern: Pattern = Pattern.compile(
            urlRegEx,
            Pattern.CASE_INSENSITIVE or Pattern.MULTILINE or Pattern.DOTALL)

    fun resolveURL(url: String, part: String): String {
        return if (URLUtil.isValidUrl(part)) {
            part
        } else {
            var baseUri: URI? = null
            try {
                baseUri = URI(url)
            } catch (e: URISyntaxException) {
                e.printStackTrace()
            }
            if (baseUri == null) return url
            baseUri = baseUri.resolve(part)
            baseUri.toString()
        }
    }

    fun getNonNullString(str: String?): String {
        return str ?: ""
    }
}