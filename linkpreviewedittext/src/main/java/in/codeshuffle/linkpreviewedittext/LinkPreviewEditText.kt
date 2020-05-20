package `in`.codeshuffle.linkpreviewedittext

import `in`.codeshuffle.linkpreviewedittext.listener.LinkPreviewListener
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import java.util.*

class LinkPreviewEditText : AppCompatEditText, LinkScraper.LinkPreviewCallback {

    private var detectLinks = false
    private var showingPreview = false
    private var previewingUrl = ""
    private var linkScraper: LinkScraper? = null
    private var linkPreviewListener: LinkPreviewListener? = null

    //Caching
    private var mEnableCache = false
    private val linkPreviewCache = HashMap<String, LinkInfo?>()

    //TextWatcher to watch text
    private val linkTextWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable) {
            if (detectLinks) {
                //If auto detect link is on, then find for links as user types
                findAndRequestLinkPreview()
            }
        }
    }

    constructor(context: Context) : super(context) {
        initView(context, null, 0)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView(context, attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView(context, attrs, defStyleAttr)
    }

    private fun initView(context: Context, attributeSet: AttributeSet?, defAttrSet: Int) {
        linkScraper = LinkScraper()
        linkScraper!!.setLinkPreviewCallback(this)
        addTextChangedListener(linkTextWatcher)
    }

    /**
     * Set listener to get callback events about link preview
     *
     * @param linkPreviewListener listener instance
     */
    fun setLinkPreviewListener(linkPreviewListener: LinkPreviewListener?) {
        this.linkPreviewListener = linkPreviewListener
    }

    /**
     * Detect urls and fetch preview as user types through EditText
     *
     * @param shouldDetectLinks true or false
     */
    fun detectLinksWhileTyping(shouldDetectLinks: Boolean) {
        detectLinks = shouldDetectLinks
        if (detectLinks) {
            addTextChangedListener(linkTextWatcher)
        } else {
            removeTextChangedListener(linkTextWatcher)
        }
    }

    /**
     * Find through the text entered in EditText and fetch preview if possible
     */
    fun findAndRequestLinkPreview() {
        val urlEditable = text
        if (urlEditable == null) {
            linkPreviewListener?.onLinkPreviewError("Something went wrong")
        } else {
            val content = urlEditable.toString()

            //If empty content after text change, close the preview no matter what
            if (content.isEmpty()) {
                closeLinkPreview()
            } else {

                //Split based on whitespace
                val parts = content.split("\\s+").toTypedArray()
                // iterate through parts
                for (item in parts) {
                    //If url pattern is matched, and the looking up url is not the previously fetched url, close the preview and make a search
                    if (Utils.urlPattern.matcher(item).matches()) {
                        if (previewingUrl != item) {
                            if (showingPreview) closeLinkPreview()
                            linkPreviewListener?.onLinkFound(item)
                            //Check in cache
                            if (mEnableCache && linkPreviewCache.containsKey(item)) {
                                if (linkPreviewListener != null) {
                                    onShowPreview(item, linkPreviewCache[item], true)
                                }
                                return
                            }
                            findExactLinkPreview(item)
                        }
                        //Return must be in the first url match, because we only show the first url preview
                        return
                    }
                }

                //No links found in the text entered, so close the preview
                closeLinkPreview()
            }
        }
    }

    /**
     * Closes the existing preview, sets the preview Url empty and showingPreview bool to false
     * and notifies the listener if there was a preview being shown
     */
    private fun closeLinkPreview() {
        linkPreviewListener?.onNoLinkPreview()
        previewingUrl = ""
        showingPreview = false
    }

    /**
     * Find preview for exact specified url
     *
     * @param url url to fetch preview of
     */
    private fun findExactLinkPreview(url: String) {
        linkScraper!!.getLinkPreview(url)
    }

    /**
     * Explicit method available for user, usable when needing to close the preview on their external UI (for example) button click
     */
    fun closePreview() {
        closeLinkPreview()
    }

    fun setCacheEnabled(enableCache: Boolean) {
        mEnableCache = enableCache
        if (mEnableCache) {
            linkPreviewCache.clear()
        } else {
            linkPreviewCache.clear()
        }
    }

    override fun onShowNoPreview() {
        if (!showingPreview) closeLinkPreview()
    }

    override fun onShowPreview(previewUrl: String, linkInfo: LinkInfo?, fetchedFromCache: Boolean) {
        showingPreview = true
        previewingUrl = previewUrl
        if (mEnableCache && !fetchedFromCache) {
            linkPreviewCache[previewUrl] = linkInfo
        }
        linkPreviewListener?.onLinkPreviewFound(linkInfo, fetchedFromCache)
    }

    override fun onPreviewError(errorMsg: String) {
        if (!showingPreview) linkPreviewListener?.onLinkPreviewError(errorMsg)
    }
}