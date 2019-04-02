package in.codeshuffle.linkpreviewedittext.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import in.codeshuffle.linkpreviewedittext.R;
import in.codeshuffle.linkpreviewedittext.listener.LinkPreviewListener;
import in.codeshuffle.linkpreviewedittext.model.LinkInfo;
import in.codeshuffle.linkpreviewedittext.scraper.LinkScraper;
import in.codeshuffle.linkpreviewedittext.util.GlideApp;

public class LinkPreviewEditText extends LinearLayout {

    private View view;
    private LinearLayout linearLayout;
    private ImageView imageView;
    private TextView textViewTitle;
    private TextView textViewDesc;
    private TextView textViewUrl;

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
        setupView(context);
        getViewReferences();
        setVisibility(GONE);
        new LinkScraper(new LinkPreviewListener() {
            @Override
            public void onLinkDetails(LinkInfo linkInfo) {
                GlideApp.with(context)
                        .load(linkInfo.getImageUrl())
                        .addListener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                setVisibility(VISIBLE);
                                return false;
                            }
                        })
                        .into(imageView);
                Log.d("NICE", linkInfo.toString());
            }

            @Override
            public void onError(Exception e) {
                Log.d("NICE", "onError: Not nice");
            }
        }).getLinkPreview("https://bintray.com/skymansandy");
    }

    private void getViewReferences() {
        linearLayout = findViewById(R.id.link_info_card);
        imageView = findViewById(R.id.link_image);
        textViewTitle = findViewById(R.id.link_title);
        textViewDesc = findViewById(R.id.link_desc);
        textViewUrl = findViewById(R.id.link_url);
    }

    /**
     * @param context context of the view
     */
    private void setupView(Context context) {
        if (findLinearLayoutChild() != null) {
            this.view = findLinearLayoutChild();
        } else {
            this.view = this;
            inflate(context, R.layout.link_info_whatsapp_style, this);
        }
    }

    protected LinearLayout findLinearLayoutChild() {
        if (getChildCount() > 0 && getChildAt(0) instanceof LinearLayout) {
            return (LinearLayout) getChildAt(0);
        }
        return null;
    }
}
