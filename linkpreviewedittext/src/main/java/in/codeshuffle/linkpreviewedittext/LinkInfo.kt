package `in`.codeshuffle.linkpreviewedittext

data class LinkInfo(
        var url: String? = null,
        var domainUrl: String? = null,
        var title: String? = null,
        var description: String? = null,
        var imageUrl: String? = null,
        var siteName: String? = null,
        var mediaType: String? = null,
        var faviconUrl: String? = null
)