package `in`.codeshuffle.linkpreviewedittext

import android.os.AsyncTask
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.IOException
import java.text.MessageFormat

internal class LinkScraper {

    private var linkPreviewCallback: LinkPreviewCallback? = null

    fun setLinkPreviewCallback(linkPreviewCallback: LinkPreviewCallback?) {
        this.linkPreviewCallback = linkPreviewCallback
    }

    fun getLinkPreview(url: String) {
        LinkParser(url, linkPreviewCallback).execute()
    }

    /**
     * AsyncTask for parsing stuff
     */
    private class LinkParser internal constructor(private val linkUrl: String, linkPreviewCallback: LinkPreviewCallback?) : AsyncTask<Void?, Void?, String?>() {
        private var linkInfo: LinkInfo?
        private val linkPreviewCallback: LinkPreviewCallback?

        override fun doInBackground(vararg params: Void?): String? {
            val document: Document
            try {
                document = Jsoup.connect(linkUrl).timeout(30 * 1000).get()
                linkInfo = ParserUtils(linkUrl)
                        .parseLinkDataModular(document)
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
                return "Must supply valid url"
            } catch (e: IOException) {
                e.printStackTrace()
                return MessageFormat.format("No Html Received from {0}", linkUrl)
            } catch (e: Exception) {
                return e.localizedMessage
            }
            return null
        }

        override fun onPostExecute(aString: String?) {
            super.onPostExecute(aString)
            //Success
            if (aString == null) {
                if (linkInfo?.title == null || linkInfo?.description == null || linkInfo?.domainUrl == null) return
                throwSuccess(linkInfo)
            } else {
                throwError(aString)
            }
        }

        /**
         * Successfully fetched link preview
         *
         * @param linkInfo link info object
         */
        private fun throwSuccess(linkInfo: LinkInfo?) {
            linkPreviewCallback?.onShowPreview(linkUrl, linkInfo, false)
        }

        /**
         * Link preview fetching error
         *
         * @param errorMsg error message
         */
        private fun throwError(errorMsg: String) {
            if (linkPreviewCallback != null) {
                linkPreviewCallback.onPreviewError(errorMsg)
                linkPreviewCallback.onShowNoPreview()
            }
        }

        init {
            linkInfo = LinkInfo()
            this.linkPreviewCallback = linkPreviewCallback
        }
    }


    internal interface LinkPreviewCallback {
        fun onShowNoPreview()
        fun onShowPreview(previewUrl: String, linkInfo: LinkInfo?, fromCache: Boolean)
        fun onPreviewError(errorMsg: String)
    }
}