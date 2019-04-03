package in.codeshuffle.linkpreviewedittext;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URI;
import java.net.URISyntaxException;

class DocumentUtils {

    /**
     * Get title of the document
     *
     * @param document document object
     * @return description of the url
     */
    static String getLinkTitle(Document document) {
        String title = document.select("meta[property=og:title]")
                .attr("content");
        if (title == null || title.isEmpty()) {
            title = document.title();
        }
        return title;
    }

    /**
     * Get description of the document
     *
     * @param document document object
     * @return description of the url
     */
    static String getDescription(Document document) {
        String description = document.select("meta[name=description]")
                .attr("content");
        if (description == null || description.isEmpty()) {
            description = document.select("meta[name=Description]").attr("content");
        }
        if (description == null || description.isEmpty()) {
            description = document.select("meta[property=og:description]").attr("content");
        }
        if (description == null || description.isEmpty()) {
            description = "";
        }
        return description;
    }

    /**
     * Get media type of the document
     *
     * @param document document object
     * @return description of the url
     */
    static String getMediaType(Document document) {
        Elements mediaTypes = document.select("meta[name=medium]");
        if (mediaTypes.size() > 0) {
            String media = mediaTypes.attr("content");
            return media.equals("image") ? "photo" : media;
        } else {
            return document.select("meta[property=og:type]").attr("content");
        }
    }

    /**
     * Get favicon of the document
     *
     * @param document document object
     * @return description of the url
     */
    static String getFaviconUrl(Document document) {
        String url = document.select("link[rel=apple-touch-icon]").attr("href");
        if (!url.isEmpty()) {
            return Utils.resolveURL(url, url);
        } else {
            url = document.select("link[rel=icon]").attr("href");
            if (!url.isEmpty()) {
                return Utils.resolveURL(url, url);
            }
        }
        return "";
    }

    /**
     * Get image url of the document
     *
     * @param linkUrl  original link url
     * @param document document object
     * @return description of the url
     */
    static String getImageUrl(String linkUrl, Document document) {
        String imageUrl = "";
        Elements imageElements = document.select("meta[property=og:image]");
        if (imageElements.size() > 0) {
            String image = imageElements.attr("content");
            if (!image.isEmpty()) {
                imageUrl = Utils.resolveURL(linkUrl, image);
            }
        }
        if (imageUrl.isEmpty()) {
            String src = document.select("link[rel=image_src]").attr("href");
            if (!src.isEmpty()) {
                imageUrl = Utils.resolveURL(linkUrl, src);
            } else {
                src = document.select("link[rel=apple-touch-icon]").attr("href");
                if (!src.isEmpty()) {
                    imageUrl = Utils.resolveURL(linkUrl, src);
                } else {
                    src = document.select("link[rel=icon]").attr("href");
                    if (!src.isEmpty()) {
                        imageUrl = Utils.resolveURL(linkUrl, src);
                    }
                }
            }
        }
        return imageUrl;
    }

    /**
     * Get domain of the document
     *
     * @param linkUrl original link
     * @param document document object
     * @return description of the url
     */
    static String getDomainUrl(String linkUrl, Document document) {
        String domainUrl = "";
        Elements elements = document.getElementsByTag("meta");
        for (Element element : elements) {
            if (element.hasAttr("property")) {
                String str_property = element.attr("property").trim();
                if (str_property.equals("og:linkUrl")) {
                    domainUrl = element.attr("content");
                }
            }
        }

        if (domainUrl.equals("") || domainUrl.isEmpty()) {
            URI uri = null;
            try {
                uri = new URI(linkUrl);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            if (uri == null) {
                domainUrl = linkUrl;
            } else {
                domainUrl = uri.getHost();
            }
        }
        return domainUrl;
    }

    /**
     * Get title of the document
     *
     * @param document document object
     * @return description of the url
     */
    static String getUrlTitle(Document document) {
        String siteName = "";
        Elements elements = document.getElementsByTag("meta");
        for (Element element : elements) {
            if (element.hasAttr("property")) {
                String str_property = element.attr("property").trim();
                if (str_property.equals("og:site_name")) {
                    siteName = element.attr("content");
                }
            }
        }
        return siteName;
    }
}
