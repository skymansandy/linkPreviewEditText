package `in`.codeshuffle.linkpreviewedittext

import org.jsoup.nodes.Document
import java.net.URI
import java.net.URISyntaxException

internal class ParserUtils(private val originalUrl: String) {

    /**
     * UnModular parse
     *
     * @param document document object
     */
    fun parseLinkDataUnModular(document: Document): LinkInfo {
        val linkInfo = LinkInfo()
        val elements = document.getElementsByTag("meta")

        // getTitle document.select("meta[property=og:title]")
        var title = document.select("meta[property=og:title]").attr("content")
        if (title == null || title.isEmpty()) {
            title = document.title()
        }
        linkInfo.title = title

        //getDescription
        var description = document.select("meta[name=description]").attr("content")
        if (description == null || description.isEmpty()) {
            description = document.select("meta[name=Description]").attr("content")
        }
        if (description == null || description.isEmpty()) {
            description = document.select("meta[property=og:description]").attr("content")
        }
        if (description == null || description.isEmpty()) {
            description = ""
        }
        linkInfo.description = description


        // getMediaType
        val mediaTypes = document.select("meta[name=medium]")
        val type: String
        type = if (mediaTypes.size > 0) {
            val media = mediaTypes.attr("content")
            if (media == "image") "photo" else media
        } else {
            document.select("meta[property=og:type]").attr("content")
        }
        linkInfo.mediaType = type


        //getImages
        val imageElements = document.select("meta[property=og:image]")
        if (imageElements.size > 0) {
            val image = imageElements.attr("content")
            if (!image.isEmpty()) {
                linkInfo.imageUrl = Utils.resolveURL(originalUrl, image)
            }
        }
        if (linkInfo.imageUrl?.isEmpty() == true) {
            var src = document.select("link[rel=image_src]").attr("href")
            if (!src.isEmpty()) {
                linkInfo.imageUrl = Utils.resolveURL(originalUrl, src)
            } else {
                src = document.select("link[rel=apple-touch-icon]").attr("href")
                if (!src.isEmpty()) {
                    linkInfo.imageUrl = Utils.resolveURL(originalUrl, src)
                    linkInfo.faviconUrl = Utils.resolveURL(originalUrl, src)
                } else {
                    src = document.select("link[rel=icon]").attr("href")
                    if (!src.isEmpty()) {
                        linkInfo.imageUrl = Utils.resolveURL(originalUrl, src)
                        linkInfo.faviconUrl = Utils.resolveURL(originalUrl, src)
                    }
                }
            }
        }

        //Favicon
        var src = document.select("link[rel=apple-touch-icon]").attr("href")
        if (!src.isEmpty()) {
            linkInfo.faviconUrl = Utils.resolveURL(originalUrl, src)
        } else {
            src = document.select("link[rel=icon]").attr("href")
            if (!src.isEmpty()) {
                linkInfo.faviconUrl = Utils.resolveURL(originalUrl, src)
            }
        }
        for (element in elements) {
            if (element.hasAttr("property")) {
                val str_property = element.attr("property").trim { it <= ' ' }
                if (str_property == "og:originalUrl") {
                    linkInfo.url = element.attr("content")
                }
                if (str_property == "og:site_name") {
                    linkInfo.siteName = element.attr("content")
                }
            }
        }
        if (linkInfo.url == "" || linkInfo.url?.isEmpty() == true) {
            var uri: URI? = null
            try {
                uri = URI(originalUrl)
            } catch (e: URISyntaxException) {
                e.printStackTrace()
            }
            if (uri == null) {
                linkInfo.url = originalUrl
            } else {
                linkInfo.url = uri.host
            }
        }
        return linkInfo
    }

    /**
     * Modular parse
     *
     * @param document document object
     */
    fun parseLinkDataModular(document: Document): LinkInfo {
        val linkInfo = LinkInfo()

        //set the original url
        linkInfo.url = originalUrl

        //parse title
        val title = DocumentUtils.getLinkTitle(document)
        linkInfo.title = Utils.getNonNullString(title)

        //parse description
        val description = DocumentUtils.getDescription(document)
        linkInfo.description = Utils.getNonNullString(description)

        //parse favicon
        val faviconUrl = DocumentUtils.getFaviconUrl(document)
        linkInfo.faviconUrl = Utils.getNonNullString(faviconUrl)

        //parse mediaType
        val type = DocumentUtils.getMediaType(document)
        linkInfo.mediaType = Utils.getNonNullString(type)


        //parse images
        val imageUrl = DocumentUtils.getImageUrl(originalUrl, document)
        linkInfo.imageUrl = Utils.getNonNullString(imageUrl)
        val domainUrl = DocumentUtils.getDomainUrl(originalUrl, document)
        linkInfo.domainUrl = Utils.getNonNullString(domainUrl)
        val siteName = DocumentUtils.getUrlTitle(document)
        linkInfo.siteName = Utils.getNonNullString(siteName)
        return linkInfo
    }

}