package in.codeshuffle.linkpreviewedittext;

import android.content.Context;

import androidx.annotation.Nullable;

import android.text.Editable;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatEditText;
import in.codeshuffle.linkpreviewedittext.listener.LinkPreviewListener;

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

    private void setLinkPreviewData(String previewUrl, boolean showingPreview) {
        this.showingPreview = showingPreview;
        if (isShowingPreview()) {
            this.previewingUrl = previewUrl;
        } else {
            this.previewingUrl = "";
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
            String content = urlEditable.toString();

            //If empty content after text change, close the preview no matter what
            if (content.length() == 0) {
                closeExistingPreview();
            }
            //Find for links otherwise
            else {

                //Split based on whitespace
                String[] parts = content.split("\\s+");
                // iterate through parts
                for (String item : parts) {
                    //If url pattern is matched, and the looking up url is not the previously fetched url, close the preview and make a search
                    if (Utils.urlPattern.matcher(item).matches()) {
                        if (!previewingUrl.equals(item)) {
                            closeExistingPreview();
                            findExactLinkPreview(item);
                        }
                        //Return must be in the first url match, because we only show the first url preview
                        return;
                    }
                }
                //No links found in the text entered, so close the preview
                closeExistingPreview();
            }
        }
    }

    /**
     * Closes the existing preview, sets the preview Url empty and showingPreview bool to false
     * and notifies the listener if there was a preview being shown
     */
    private void closeExistingPreview() {
        if (showingPreview && linkPreviewListener != null) {
            linkPreviewListener.onNoLinkPreview();
        }
        this.previewingUrl = "";
        this.showingPreview = false;
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
            //If auto detect link is on, then find for links as user types
            findAndRequestLinkPreview();
        }
    }

    @Override
    public void onPreviewDataChanged(String previewUrl, boolean isShowing) {
        setLinkPreviewData(previewUrl, isShowing);
    }

    /**
     * Explicit method available for user, usable when needing to close the preview on their external UI (for example) button click
     */
    public void closePreview() {
        closeExistingPreview();
    }
}
