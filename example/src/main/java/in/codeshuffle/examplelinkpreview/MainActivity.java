package in.codeshuffle.examplelinkpreview;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import in.codeshuffle.linkpreviewedittext.LinkInfo;
import in.codeshuffle.linkpreviewedittext.LinkPreviewEditText;
import in.codeshuffle.linkpreviewedittext.listener.LinkPreviewListener;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.linkImage)
    ImageView ivLinkImage;
    @BindView(R.id.closePreview)
    ImageView ivClosePreview;
    @BindView(R.id.linkTitle)
    TextView tvLinkTitle;
    @BindView(R.id.linkDesc)
    TextView tvLinkDesc;
    @BindView(R.id.linkUrl)
    TextView tvLinkUrl;
    @BindView(R.id.linkPreviewLayout)
    ViewGroup linkPreviewLayout;
    @BindView(R.id.linkPreviewEditText)
    LinkPreviewEditText linkPreviewEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        linkPreviewEditText.detectLinksWhileTyping(true);
        ivClosePreview.setOnClickListener(v
                -> linkPreviewEditText.closePreview());
        linkPreviewEditText.setLinkPreviewListener(new LinkPreviewListener() {
            @Override
            public void onLinkPreviewOpen(LinkInfo linkInfo) {
                Log.d(TAG, "onLinkPreviewOpen: " + linkInfo.toString());
                tvLinkTitle.setText(linkInfo.getTitle());
                tvLinkDesc.setText(linkInfo.getDescription());
                tvLinkUrl.setText(linkInfo.getUrl());
                tvLinkDesc.setVisibility(linkInfo.getDescription().length() == 0
                        ? View.GONE : View.VISIBLE);
                GlideApp.with(MainActivity.this)
                        .load(linkInfo.getImageUrl())
                        .addListener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                ivLinkImage.setVisibility(View.GONE);
                                linkPreviewLayout.setVisibility(View.VISIBLE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                ivLinkImage.setVisibility(View.VISIBLE);
                                linkPreviewLayout.setVisibility(View.VISIBLE);
                                return false;
                            }
                        })
                        .into(ivLinkImage);
            }

            @Override
            public void onNoLinkPreview() {
                Log.d(TAG, "onNoLinkPreview: Closed");
                linkPreviewLayout.setVisibility(View.GONE);
            }

            @Override
            public void onLinkPreviewError(String errorMsg) {
                Log.d(TAG, "onLinkPreviewError: " + errorMsg);
                linkPreviewLayout.setVisibility(View.GONE);
            }
        });
    }
}
