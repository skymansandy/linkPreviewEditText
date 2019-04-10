package in.codeshuffle.linkpreviewedittext;

import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.text.MessageFormat;

import in.codeshuffle.linkpreviewedittext.listener.LinkPreviewListener;

class LinkScraper {

    private LinkPreviewCallback linkPreviewCallback;

    LinkScraper() {
    }

    void setLinkPreviewCallback(LinkPreviewCallback linkPreviewCallback) {
        this.linkPreviewCallback = linkPreviewCallback;
    }

    void getLinkPreview(String url) {
        new LinkParser(url, linkPreviewCallback).execute();
    }

    interface LinkPreviewCallback {
        void onShowNoPreview();

        void onShowPreview(String previewUrl, LinkInfo linkInfo, boolean fromCache);

        void onPreviewError(String errorMsg);
    }

    /**
     * AsyncTask for parsing stuff
     */
    private static class LinkParser extends AsyncTask<Void, Void, String> {

        private String linkUrl;
        private LinkInfo linkInfo;
        private LinkPreviewCallback linkPreviewCallback;

        LinkParser(String url, LinkPreviewCallback linkPreviewCallback) {
            this.linkUrl = url;
            this.linkInfo = new LinkInfo();
            this.linkPreviewCallback = linkPreviewCallback;
        }

        @Override
        protected String doInBackground(Void... params) {
            Document document;
            try {
                document = Jsoup.connect(linkUrl).timeout(30 * 1000).get();
                linkInfo = new ParserUtils(linkUrl)
                        .parseLinkDataModular(document);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                return "Must supply valid url";
            } catch (IOException e) {
                e.printStackTrace();
                return MessageFormat.format("No Html Received from {0}", linkUrl);
            } catch (Exception e) {
                return e.getLocalizedMessage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String aString) {
            super.onPostExecute(aString);
            //Success
            if (aString == null) {
                if (linkInfo.getTitle() == null || linkInfo.getDescription() == null
                        || linkInfo.getDomainUrl() == null)
                    return;
                throwSuccess(linkInfo);
            } else {
                throwError(aString);
            }
        }

        /**
         * Successfully fetched link preview
         *
         * @param linkInfo link info object
         */
        private void throwSuccess(LinkInfo linkInfo) {
            if (linkPreviewCallback != null) {
                linkPreviewCallback.onShowPreview(linkUrl, linkInfo, false);
            }
        }

        /**
         * Link preview fetching error
         *
         * @param errorMsg error message
         */
        private void throwError(String errorMsg) {
            if (linkPreviewCallback != null) {
                linkPreviewCallback.onPreviewError(errorMsg);
                linkPreviewCallback.onShowNoPreview();
            }
        }
    }
}
