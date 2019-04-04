package in.codeshuffle.linkpreviewedittext.listener;

import in.codeshuffle.linkpreviewedittext.LinkInfo;

public interface LinkPreviewListener {
    void onLinkFound(String url);

    void onLinkPreviewOpen(LinkInfo linkInfo);

    void onNoLinkPreview();

    void onLinkPreviewError(String errorMsg);
}
