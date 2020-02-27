package in.codeshuffle.examplelinkpreview.ui.fragment.demoscreen;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import in.codeshuffle.examplelinkpreview.R;
import in.codeshuffle.examplelinkpreview.util.GlideApp;
import in.codeshuffle.linkpreviewedittext.LinkInfo;
import in.codeshuffle.linkpreviewedittext.LinkPreviewEditText;
import in.codeshuffle.linkpreviewedittext.listener.LinkPreviewListener;

public class DemoFragment extends Fragment implements LinkPreviewListener {

    private static final String TAG = DemoFragment.class.getSimpleName();

    Unbinder unbinder;

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

    public static DemoFragment getInstance() {
        Bundle bundle = new Bundle();
        DemoFragment demoFragment = new DemoFragment();
        demoFragment.setArguments(bundle);
        return demoFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_demo, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        linkPreviewEditText.detectLinksWhileTyping(true);
        ivClosePreview.setOnClickListener(v -> linkPreviewEditText.closePreview());
        linkPreviewEditText.setLinkPreviewListener(this);
        linkPreviewEditText.setCacheEnabled(true);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onLinkFound(String url) {
        Log.d(TAG, "onLinkFound: " + url);
    }

    @Override
    public void onLinkPreviewFound(LinkInfo linkInfo, boolean fromCache) {
        Log.d(TAG, "onLinkPreviewFound: " + linkInfo.toString() + " from cache=" + fromCache);
        tvLinkTitle.setText(linkInfo.getTitle());

        tvLinkDesc.setText(linkInfo.getDescription());
        tvLinkUrl.setText(linkInfo.getUrl());
        tvLinkDesc.setVisibility(linkInfo.getDescription().length() == 0
                ? View.GONE : View.VISIBLE);
        GlideApp.with(getActivity())
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
}
