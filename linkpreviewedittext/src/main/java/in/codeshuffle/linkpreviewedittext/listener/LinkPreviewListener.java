package in.codeshuffle.linkpreviewedittext.listener;

import in.codeshuffle.linkpreviewedittext.model.LinkInfo;

public interface LinkPreviewListener {
    void onLinkPreview(LinkInfo linkInfo);

    void onLinkPreviewError(String errorMsg);
}
