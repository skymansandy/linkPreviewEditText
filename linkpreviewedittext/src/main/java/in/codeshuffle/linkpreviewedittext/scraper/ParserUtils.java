package in.codeshuffle.linkpreviewedittext.scraper;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import in.codeshuffle.linkpreviewedittext.model.LinkInfo;
import in.codeshuffle.linkpreviewedittext.util.DocumentUtils;
import in.codeshuffle.linkpreviewedittext.util.Utils;

class ParserUtils {

    private final String originalUrl;

    ParserUtils(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    /**
     * UnModular parse
     *
     * @param document document object
     */
    LinkInfo parseLinkDataUnModular(Document document) {
        LinkInfo linkInfo = new LinkInfo();

        Elements elements = document.getElementsByTag("meta");

        // getTitle document.select("meta[property=og:title]")
        String title = document.select("meta[property=og:title]").attr("content");

        if (title == null || title.isEmpty()) {
            title = document.title();
        }
        linkInfo.setTitle(title);

        //getDescription
        String description = document.select("meta[name=description]").attr("content");
        if (description == null || description.isEmpty()) {
            description = document.select("meta[name=Description]").attr("content");
        }
        if (description == null || description.isEmpty()) {
            description = document.select("meta[property=og:description]").attr("content");
        }
        if (description == null || description.isEmpty()) {
            description = "";
        }
        linkInfo.setDescription(description);


        // getMediaType
        Elements mediaTypes = document.select("meta[name=medium]");
        String type;
        if (mediaTypes.size() > 0) {
            String media = mediaTypes.attr("content");

            type = media.equals("image") ? "photo" : media;
        } else {
            type = document.select("meta[property=og:type]").attr("content");
        }
        linkInfo.setMediaType(type);


        //getImages
        Elements imageElements = document.select("meta[property=og:image]");
        if (imageElements.size() > 0) {
            String image = imageElements.attr("content");
            if (!image.isEmpty()) {
                linkInfo.setImageUrl(Utils.resolveURL(originalUrl, image));
            }
        }
        if (linkInfo.getImageUrl().isEmpty()) {
            String src = document.select("link[rel=image_src]").attr("href");
            if (!src.isEmpty()) {
                linkInfo.setImageUrl(Utils.resolveURL(originalUrl, src));
            } else {
                src = document.select("link[rel=apple-touch-icon]").attr("href");
                if (!src.isEmpty()) {
                    linkInfo.setImageUrl(Utils.resolveURL(originalUrl, src));
                    linkInfo.setFaviconUrl(Utils.resolveURL(originalUrl, src));
                } else {
                    src = document.select("link[rel=icon]").attr("href");
                    if (!src.isEmpty()) {
                        linkInfo.setImageUrl(Utils.resolveURL(originalUrl, src));
                        linkInfo.setFaviconUrl(Utils.resolveURL(originalUrl, src));
                    }
                }
            }
        }

        //Favicon
        String src = document.select("link[rel=apple-touch-icon]").attr("href");
        if (!src.isEmpty()) {
            linkInfo.setFaviconUrl(Utils.resolveURL(originalUrl, src));
        } else {
            src = document.select("link[rel=icon]").attr("href");
            if (!src.isEmpty()) {
                linkInfo.setFaviconUrl(Utils.resolveURL(originalUrl, src));
            }
        }

        for (Element element : elements) {
            if (element.hasAttr("property")) {
                String str_property = element.attr("property").trim();
                if (str_property.equals("og:originalUrl")) {
                    linkInfo.setUrl(element.attr("content"));
                }
                if (str_property.equals("og:site_name")) {
                    linkInfo.setSiteName(element.attr("content"));
                }
            }
        }

        if (linkInfo.getUrl().equals("") || linkInfo.getUrl().isEmpty()) {
            URI uri = null;
            try {
                uri = new URI(originalUrl);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            if (uri == null) {
                linkInfo.setUrl(originalUrl);
            } else {
                linkInfo.setUrl(uri.getHost());
            }
        }

        return linkInfo;
    }

    /**
     * Modular parse
     *
     * @param document document object
     */
    LinkInfo parseLinkDataModular(Document document) {
        LinkInfo linkInfo = new LinkInfo();

        //set the original url
        linkInfo.setUrl(originalUrl);

        //parse title
        String title = DocumentUtils.getLinkTitle(document);
        linkInfo.setTitle(title);

        //parse description
        String description = DocumentUtils.getDescription(document);
        linkInfo.setDescription(description);

        //parse favicon
        String faviconUrl = DocumentUtils.getFaviconUrl(document);
        linkInfo.setFaviconUrl(faviconUrl);

        //parse mediaType
        String type = DocumentUtils.getMediaType(document);
        linkInfo.setMediaType(type);


        //parse images
        String imageUrl = DocumentUtils.getImageUrl(originalUrl, document);
        linkInfo.setImageUrl(imageUrl);

        String domainUrl = DocumentUtils.getDomainUrl(originalUrl, document);
        linkInfo.setDomainUrl(domainUrl);

        String siteName = DocumentUtils.getUrlTitle(document);
        linkInfo.setSiteName(siteName);

        return linkInfo;
    }
}
