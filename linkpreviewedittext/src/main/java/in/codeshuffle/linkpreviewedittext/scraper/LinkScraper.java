package in.codeshuffle.linkpreviewedittext.scraper;

import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.text.MessageFormat;

import in.codeshuffle.linkpreviewedittext.listener.LinkPreviewListener;
import in.codeshuffle.linkpreviewedittext.model.LinkInfo;

public class LinkScraper {

    private LinkPreviewListener linkPreviewListener;
    private LinkPreviewCallback linkPreviewCallback;

    public LinkScraper() {
    }

    public void setLinkPreviewListener(LinkPreviewListener linkPreviewListener) {
        this.linkPreviewListener = linkPreviewListener;
    }

    public void setLinkPreviewCallback(LinkPreviewCallback linkPreviewCallback) {
        this.linkPreviewCallback = linkPreviewCallback;
    }

    public void getLinkPreview(String url) {
        new LinkParser(url, linkPreviewListener, linkPreviewCallback).execute();
    }

    public interface LinkPreviewCallback {
        void onPreviewDataChanged(String previewUrl, boolean isShowing);
    }

    /**
     * AsyncTask for parsing stuff
     */
    private static class LinkParser extends AsyncTask<Void, Void, Void> {

        private String linkUrl;
        private LinkInfo linkInfo;
        private LinkPreviewListener linkPreviewListener;
        private LinkPreviewCallback linkPreviewCallback;

        LinkParser(String url, LinkPreviewListener linkPreviewListener, LinkPreviewCallback linkPreviewCallback) {
            this.linkUrl = url;
            this.linkInfo = new LinkInfo();
            this.linkPreviewListener = linkPreviewListener;
            this.linkPreviewCallback = linkPreviewCallback;
        }

        @Override
        protected Void doInBackground(Void... params) {
            Document document;
            try {
                document = Jsoup.connect(linkUrl).timeout(30 * 1000).get();
                linkInfo = new ParserUtils(linkUrl)
                        .parseLinkDataModular(document);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                throwError("Must supply valid url");
            } catch (IOException e) {
                e.printStackTrace();
                throwError(MessageFormat.format("No Html Received from {0}", linkUrl));
            } catch (Exception e) {
                throwError(e.getLocalizedMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            throwSuccess(linkInfo);
        }

        /**
         * Successfully fetched link preview
         *
         * @param linkInfo link info object
         */
        private void throwSuccess(LinkInfo linkInfo) {
            if (linkPreviewCallback != null) {
                linkPreviewCallback.onPreviewDataChanged(linkUrl, true);
            }
            if (linkPreviewListener != null) {
                linkPreviewListener.onLinkPreview(linkInfo);
            }
        }

        /**
         * Link preview fetching error
         *
         * @param errorMsg error message
         */
        private void throwError(String errorMsg) {
            if (linkPreviewCallback != null) {
                linkPreviewCallback.onPreviewDataChanged("", false);
            }
            if (linkPreviewListener != null) {
                linkPreviewListener.onLinkPreviewError(errorMsg);
            }
        }
    }
}
