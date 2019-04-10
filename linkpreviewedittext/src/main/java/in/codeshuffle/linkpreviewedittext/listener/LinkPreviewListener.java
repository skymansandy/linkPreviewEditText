package in.codeshuffle.linkpreviewedittext.listener;

import in.codeshuffle.linkpreviewedittext.LinkInfo;

public interface LinkPreviewListener {
    void onLinkFound(String url);

    void onLinkPreviewFound(LinkInfo linkInfo, boolean fromCache);

    void onNoLinkPreview();

    void onLinkPreviewError(String errorMsg);
}
