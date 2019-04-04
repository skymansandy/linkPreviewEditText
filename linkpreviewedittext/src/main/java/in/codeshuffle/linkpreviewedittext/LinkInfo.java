package in.codeshuffle.linkpreviewedittext;


import android.support.annotation.NonNull;

public class LinkInfo {
    private String url;
    private String domainUrl;
    private String title;
    private String description;
    private String imageUrl;
    private String siteName;
    private String mediaType;
    private String faviconUrl;

    public String getUrl() {
        return url;
    }

    void setUrl(String url) {
        this.url = url;
    }

    public String getDomainUrl() {
        return domainUrl;
    }

    void setDomainUrl(String domainUrl) {
        this.domainUrl = domainUrl;
    }

    public String getTitle() {
        return title;
    }

    void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getSiteName() {
        return siteName;
    }

    void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getMediaType() {
        return mediaType;
    }

    void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getFaviconUrl() {
        return faviconUrl;
    }

    void setFaviconUrl(String faviconUrl) {
        this.faviconUrl = faviconUrl;
    }

    @NonNull
    @Override
    public String toString() {
        return "LinkInfo{" +
                "domainUrl='" + domainUrl + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", siteName='" + siteName + '\'' +
                ", mediaType='" + mediaType + '\'' +
                ", faviconUrl='" + faviconUrl + '\'' +
                '}';
    }
}
