package in.codeshuffle.examplelinkpreview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import in.codeshuffle.linkpreviewedittext.listener.LinkPreviewListener;
import in.codeshuffle.linkpreviewedittext.LinkInfo;
import in.codeshuffle.linkpreviewedittext.LinkPreviewEditText;

public class MainActivity extends AppCompatActivity implements LinkPreviewListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinkPreviewEditText linkPreviewEditText = findViewById(R.id.linkPreviewEditText);
        linkPreviewEditText.setLinkPreviewListener(this);
        linkPreviewEditText.detectLinksWhileTyping(true);
    }

    @Override
    public void onLinkPreviewOpen(LinkInfo linkInfo) {
        Log.d("NICE", linkInfo.toString());
    }

    @Override
    public void onNoLinkPreview() {
        Log.d("NICE", "Preview closed");
    }

    @Override
    public void onLinkPreviewError(String errorMsg) {
        Log.d("NICE", errorMsg);
    }
}
