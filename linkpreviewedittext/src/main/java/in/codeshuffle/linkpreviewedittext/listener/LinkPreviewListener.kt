package `in`.codeshuffle.linkpreviewedittext.listener

import `in`.codeshuffle.linkpreviewedittext.LinkInfo

interface LinkPreviewListener {
    fun onLinkFound(url: String)
    fun onLinkPreviewFound(linkInfo: LinkInfo?, fromCache: Boolean)
    fun onNoLinkPreview()
    fun onLinkPreviewError(errorMsg: String)
}