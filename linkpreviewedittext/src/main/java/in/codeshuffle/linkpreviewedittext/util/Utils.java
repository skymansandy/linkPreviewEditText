package in.codeshuffle.linkpreviewedittext.util;

import android.webkit.URLUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Pattern;

public class Utils {

    private static String urlRegEx = "(?:^|[\\W])((ht|f)tp(s?):\\/\\/|www\\.)"
            + "(([\\w\\-]+\\.){1,}?([\\w\\-.~]+\\/?)*"
            + "[\\p{Alnum}.,%_=?&#\\-+()\\[\\]\\*$~@!:/{};']*)";
    public static final Pattern urlPattern = Pattern.compile(
            urlRegEx,
            Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);

    public static String resolveURL(String url, String part) {
        if (URLUtil.isValidUrl(part)) {
            return part;
        } else {
            URI baseUri = null;
            try {
                baseUri = new URI(url);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            if (baseUri == null) return url;

            baseUri = baseUri.resolve(part);
            return baseUri.toString();
        }
    }
}
