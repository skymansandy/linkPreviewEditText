package in.codeshuffle.linkpreviewedittext;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;

import java.util.HashMap;

import in.codeshuffle.linkpreviewedittext.listener.LinkPreviewListener;

public class LinkPreviewEditText extends AppCompatEditText implements LinkScraper.LinkPreviewCallback {

    private boolean detectLinks = false;
    private boolean showingPreview = false;
    private String previewingUrl = "";
    private LinkScraper linkScraper;
    private LinkPreviewListener linkPreviewListener;

    //Caching
    private boolean mEnableCache = false;
    private HashMap<String, LinkInfo> linkPreviewCache = new HashMap<>();

    //TextWatcher to watch text
    private TextWatcher linkTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (detectLinks) {
                //If auto detect link is on, then find for links as user types
                findAndRequestLinkPreview();
            }
        }
    };


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
        addTextChangedListener(linkTextWatcher);
    }

    /**
     * Set listener to get callback events about link preview
     *
     * @param linkPreviewListener listener instance
     */
    public void setLinkPreviewListener(LinkPreviewListener linkPreviewListener) {
        this.linkPreviewListener = linkPreviewListener;
    }

    /**
     * Detect urls and fetch preview as user types through EditText
     *
     * @param shouldDetectLinks true or false
     */
    public void detectLinksWhileTyping(boolean shouldDetectLinks) {
        this.detectLinks = shouldDetectLinks;
        if (detectLinks) {
            addTextChangedListener(linkTextWatcher);
        } else {
            removeTextChangedListener(linkTextWatcher);
        }
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
                closeLinkPreview();
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
                            if (showingPreview) closeLinkPreview();
                            if (linkPreviewListener != null)
                                linkPreviewListener.onLinkFound(item);
                            //Check in cache
                            if (mEnableCache && linkPreviewCache.containsKey(item)) {
                                if (linkPreviewListener != null) {
                                    onShowPreview(item, linkPreviewCache.get(item), true);
                                }
                                return;
                            }
                            findExactLinkPreview(item);
                        }
                        //Return must be in the first url match, because we only show the first url preview
                        return;
                    }
                }

                //No links found in the text entered, so close the preview
                closeLinkPreview();
            }
        }
    }


    /**
     * Closes the existing preview, sets the preview Url empty and showingPreview bool to false
     * and notifies the listener if there was a preview being shown
     */
    private void closeLinkPreview() {
        if (linkPreviewListener != null) {
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

    /**
     * Explicit method available for user, usable when needing to close the preview on their external UI (for example) button click
     */
    public void closePreview() {
        closeLinkPreview();
    }

    public void setCacheEnabled(boolean enableCache) {
        this.mEnableCache = enableCache;
        if (mEnableCache) {
            linkPreviewCache.clear();
        } else {
            linkPreviewCache.clear();
        }
    }

    @Override
    public void onShowNoPreview() {
        if (!showingPreview)
            closeLinkPreview();
    }

    @Override
    public void onShowPreview(String previewUrl, LinkInfo linkInfo, boolean fetchedFromCache) {
        this.showingPreview = true;
        this.previewingUrl = previewUrl;
        if (mEnableCache && !fetchedFromCache) {
            linkPreviewCache.put(previewUrl, linkInfo);
        }
        linkPreviewListener.onLinkPreviewFound(linkInfo, fetchedFromCache);
    }

    @Override
    public void onPreviewError(String errorMsg) {
        if (!showingPreview)
            linkPreviewListener.onLinkPreviewError(errorMsg);
    }
}
