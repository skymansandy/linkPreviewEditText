package in.codeshuffle.linkpreviewedittext.scraper;

import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

import in.codeshuffle.linkpreviewedittext.listener.LinkPreviewListener;
import in.codeshuffle.linkpreviewedittext.model.LinkInfo;

public class LinkScraper {

    private LinkPreviewListener linkPreviewListener;

    public LinkScraper(LinkPreviewListener linkPreviewListener) {
        this.linkPreviewListener = linkPreviewListener;
    }

    public void getLinkPreview(String url) {
        new LinkParser(url, linkPreviewListener).execute();
    }

    /**
     * AsyncTask for parsing stuff
     */
    private static class LinkParser extends AsyncTask<Void, Void, Void> {

        private String linkUrl;
        private LinkInfo linkInfo;
        private LinkPreviewListener linkPreviewListener;

        LinkParser(String url, LinkPreviewListener linkPreviewListener) {
            this.linkUrl = url;
            this.linkInfo = new LinkInfo();
            this.linkPreviewListener = linkPreviewListener;
        }

        @Override
        protected Void doInBackground(Void... params) {
            Document document;
            try {
                document = Jsoup.connect(linkUrl).timeout(30 * 1000).get();
                linkInfo = new ParserUtils(linkUrl)
                        .parseLinkDataModular(document);
            } catch (IOException e) {
                e.printStackTrace();
                linkPreviewListener
                        .onError(new Exception("No Html Received from " + linkUrl + " Check your Internet " + e.getLocalizedMessage()));
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            linkPreviewListener.onLinkDetails(linkInfo);
        }
    }
}
