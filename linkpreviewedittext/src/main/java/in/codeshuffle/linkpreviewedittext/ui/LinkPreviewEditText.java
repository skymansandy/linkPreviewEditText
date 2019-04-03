package in.codeshuffle.linkpreviewedittext.ui;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.util.AttributeSet;

import in.codeshuffle.linkpreviewedittext.listener.LinkPreviewListener;
import in.codeshuffle.linkpreviewedittext.scraper.LinkScraper;
import in.codeshuffle.linkpreviewedittext.util.Utils;

public class LinkPreviewEditText extends AppCompatEditText implements LinkScraper.LinkPreviewCallback {

    private boolean detectLinks = false;
    private boolean showingPreview = false;
    private String previewingUrl = "";
    private LinkPreviewListener linkPreviewListener;
    private LinkScraper linkScraper;

    public LinkPreviewEditText(Context context) {
        super(context);
        initView(context, null, 0);
    }

    public LinkPreviewEditText(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs, 0);
    }

    public LinkPreviewEditText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs, defStyleAttr);
    }

    private void initView(final Context context, AttributeSet attributeSet, int defAttrSet) {
        linkScraper = new LinkScraper();
        linkScraper.setLinkPreviewCallback(this);
    }

    public boolean isShowingPreview() {
        return showingPreview;
    }

    public void setShowingPreview(String previewUrl, boolean showingPreview) {
        this.showingPreview = showingPreview;
        if (isShowingPreview()) {
            this.previewingUrl = previewUrl;
        }
    }

    public void setLinkPreviewListener(LinkPreviewListener linkPreviewListener) {
        this.linkPreviewListener = linkPreviewListener;
        this.linkScraper.setLinkPreviewListener(linkPreviewListener);
    }

    /**
     * Detect urls and fetch preview as user types through EditText
     *
     * @param shouldDetectLinks true or false
     */
    public void detectLinksWhileTyping(boolean shouldDetectLinks) {
        this.detectLinks = shouldDetectLinks;
    }


    /**
     * Find through the text entered in EditText and fetch preview if possible
     */
    public void findAndRequestLinkPreview() {
        Editable urlEditable = getText();
        if (urlEditable == null) {
            if (linkPreviewListener != null)
                linkPreviewListener.onLinkPreviewError("Something went wrong");
        } else {
            String[] parts = urlEditable.toString().split("\\s+");
            // iterate through parts
            for (String item : parts) {
                if (Utils.urlPattern.matcher(item).matches()) {
                    if (!showingPreview || !previewingUrl.equals(item))
                        findExactLinkPreview(item);
                    return;
                }
            }
            if (linkPreviewListener != null) {
                linkPreviewListener.onLinkPreviewError("No URL found");
            }
        }
    }

    /**
     * Find preview for exact specified url
     *
     * @param url url to fetch preview of
     */
    private void findExactLinkPreview(String url) {
        linkScraper.getLinkPreview(url);
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        if (detectLinks) {
            findAndRequestLinkPreview();
        }
    }

    @Override
    public void onPreviewDataChanged(String previewUrl, boolean isShowing) {
        setShowingPreview(previewUrl, isShowing);
    }
}
