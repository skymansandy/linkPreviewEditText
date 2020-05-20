package `in`.codeshuffle.linkpreviewedittext

import org.jsoup.nodes.Document
import java.net.URI
import java.net.URISyntaxException

internal object DocumentUtils {

    /**
     * Get title of the document
     *
     * @param document document object
     * @return description of the url
     */
    fun getLinkTitle(document: Document): String? {
        var title = document.select("meta[property=og:title]")
                .attr("content")
        if (title == null || title.isEmpty()) {
            title = document.title()
        }
        return title
    }

    /**
     * Get description of the document
     *
     * @param document document object
     * @return description of the url
     */
    fun getDescription(document: Document): String {
        var description = document.select("meta[name=description]")
                .attr("content")
        if (description == null || description.isEmpty()) {
            description = document.select("meta[name=Description]").attr("content")
        }
        if (description == null || description.isEmpty()) {
            description = document.select("meta[property=og:description]").attr("content")
        }
        if (description == null || description.isEmpty()) {
            description = ""
        }
        return description
    }

    /**
     * Get media type of the document
     *
     * @param document document object
     * @return description of the url
     */
    fun getMediaType(document: Document): String {
        val mediaTypes = document.select("meta[name=medium]")
        return if (mediaTypes.size > 0) {
            val media = mediaTypes.attr("content")
            if (media == "image") "photo" else media
        } else {
            document.select("meta[property=og:type]").attr("content")
        }
    }

    /**
     * Get favicon of the document
     *
     * @param document document object
     * @return description of the url
     */
    fun getFaviconUrl(document: Document): String? {
        var url = document.select("link[rel=apple-touch-icon]").attr("href")
        if (url.isNotEmpty()) {
            return Utils.resolveURL(url, url)
        } else {
            url = document.select("link[rel=icon]").attr("href")
            if (url.isNotEmpty()) {
                return Utils.resolveURL(url, url)
            }
        }
        return ""
    }

    /**
     * Get image url of the document
     *
     * @param linkUrl  original link url
     * @param document document object
     * @return description of the url
     */
    fun getImageUrl(linkUrl: String, document: Document): String? {
        var imageUrl: String? = ""
        val imageElements = document.select("meta[property=og:image]")
        if (imageElements.size > 0) {
            val image = imageElements.attr("content")
            if (image.isNotEmpty()) {
                imageUrl = Utils.resolveURL(linkUrl, image)
            }
        }
        if (imageUrl!!.isEmpty()) {
            var src = document.select("link[rel=image_src]").attr("href")
            if (src.isNotEmpty()) {
                imageUrl = Utils.resolveURL(linkUrl, src)
            } else {
                src = document.select("link[rel=apple-touch-icon]").attr("href")
                if (src.isNotEmpty()) {
                    imageUrl = Utils.resolveURL(linkUrl, src)
                } else {
                    src = document.select("link[rel=icon]").attr("href")
                    if (src.isNotEmpty()) {
                        imageUrl = Utils.resolveURL(linkUrl, src)
                    }
                }
            }
        }
        return imageUrl
    }

    /**
     * Get domain of the document
     *
     * @param linkUrl original link
     * @param document document object
     * @return description of the url
     */
    fun getDomainUrl(linkUrl: String, document: Document): String {
        var domainUrl = ""
        val elements = document.getElementsByTag("meta")
        for (element in elements) {
            if (element.hasAttr("property")) {
                val strProperty = element.attr("property").trim { it <= ' ' }
                if (strProperty == "og:linkUrl") {
                    domainUrl = element.attr("content")
                }
            }
        }
        if (domainUrl == "" || domainUrl.isEmpty()) {
            var uri: URI? = null
            try {
                uri = URI(linkUrl)
            } catch (e: URISyntaxException) {
                e.printStackTrace()
            }
            domainUrl = if (uri == null) {
                linkUrl
            } else {
                uri.host
            }
        }
        return domainUrl
    }

    /**
     * Get title of the document
     *
     * @param document document object
     * @return description of the url
     */
    fun getUrlTitle(document: Document): String {
        var siteName = ""
        val elements = document.getElementsByTag("meta")
        for (element in elements) {
            if (element.hasAttr("property")) {
                val strProperty = element.attr("property").trim { it <= ' ' }
                if (strProperty == "og:site_name") {
                    siteName = element.attr("content")
                }
            }
        }
        return siteName
    }
}