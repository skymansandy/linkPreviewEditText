package in.codeshuffle.linkpreviewedittext.listener;

import in.codeshuffle.linkpreviewedittext.model.LinkInfo;

public interface LinkPreviewListener {
    void onLinkDetails(LinkInfo linkInfo);

    void onError(Exception e);
}
