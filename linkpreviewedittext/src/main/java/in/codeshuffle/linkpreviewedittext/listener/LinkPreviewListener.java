package in.codeshuffle.linkpreviewedittext.listener;

import in.codeshuffle.linkpreviewedittext.LinkInfo;

public interface LinkPreviewListener {
    void onLinkFound(String url);

    void onLinkPreviewFound(LinkInfo linkInfo);

    void onNoLinkPreview();

    void onLinkPreviewError(String errorMsg);
}
