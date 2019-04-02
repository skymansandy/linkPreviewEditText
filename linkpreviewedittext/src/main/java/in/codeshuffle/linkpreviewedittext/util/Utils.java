package in.codeshuffle.linkpreviewedittext.util;

import android.webkit.URLUtil;

import java.net.URI;
import java.net.URISyntaxException;

public class Utils {
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
