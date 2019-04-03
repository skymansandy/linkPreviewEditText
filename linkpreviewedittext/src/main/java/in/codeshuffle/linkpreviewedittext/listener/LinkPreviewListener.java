package in.codeshuffle.linkpreviewedittext.listener;

import in.codeshuffle.linkpreviewedittext.model.LinkInfo;

public interface LinkPreviewListener {
    void onLinkPreviewOpen(LinkInfo linkInfo);

    void onNoLinkPreview();

    void onLinkPreviewError(String errorMsg);
}
