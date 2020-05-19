package in.codeshuffle.examplelinkpreview.ui.fragment.demoscreen;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.jetbrains.annotations.NotNull;

import in.codeshuffle.examplelinkpreview.databinding.FragmentDemoBinding;
import in.codeshuffle.examplelinkpreview.util.GlideApp;
import in.codeshuffle.linkpreviewedittext.LinkInfo;
import in.codeshuffle.linkpreviewedittext.listener.LinkPreviewListener;

public class DemoFragment extends Fragment implements LinkPreviewListener {

    private static final String TAG = DemoFragment.class.getSimpleName();

    private FragmentDemoBinding binding;

    public static DemoFragment getInstance() {
        Bundle bundle = new Bundle();
        DemoFragment demoFragment = new DemoFragment();
        demoFragment.setArguments(bundle);
        return demoFragment;
    }

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDemoBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.linkPreviewEditText.detectLinksWhileTyping(true);
        binding.linkPreviewLayout.closePreview.setOnClickListener(v -> binding.linkPreviewEditText.closePreview());
        binding.linkPreviewEditText.setLinkPreviewListener(this);
        binding.linkPreviewEditText.setCacheEnabled(true);
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
    public void onLinkFound(String url) {
        Log.d(TAG, "onLinkFound: " + url);
    }

    @Override
    public void onLinkPreviewFound(LinkInfo linkInfo, boolean fromCache) {
        if (getActivity() != null) {
            Log.d(TAG, "onLinkPreviewFound: " + linkInfo.toString() + " from cache=" + fromCache);
            binding.linkPreviewLayout.linkTitle.setText(linkInfo.getTitle());

            binding.linkPreviewLayout.linkDesc.setText(linkInfo.getDescription());
            binding.linkPreviewLayout.linkUrl.setText(linkInfo.getUrl());
            binding.linkPreviewLayout.linkDesc.setVisibility(linkInfo.getDescription().length() == 0
                    ? View.GONE : View.VISIBLE);
            GlideApp.with(getActivity())
                    .load(linkInfo.getImageUrl())
                    .addListener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            binding.linkPreviewLayout.linkImage.setVisibility(View.GONE);
                            binding.linkPreviewLayout.getRoot().setVisibility(View.VISIBLE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            binding.linkPreviewLayout.linkImage.setVisibility(View.VISIBLE);
                            binding.linkPreviewLayout.getRoot().setVisibility(View.VISIBLE);
                            return false;
                        }
                    })
                    .into(binding.linkPreviewLayout.linkImage);
        }
    }

    @Override
    public void onNoLinkPreview() {
        Log.d(TAG, "onNoLinkPreview: Closed");
        binding.linkPreviewLayout.getRoot().setVisibility(View.GONE);
    }

    @Override
    public void onLinkPreviewError(String errorMsg) {
        Log.d(TAG, "onLinkPreviewError: " + errorMsg);
        binding.linkPreviewLayout.getRoot().setVisibility(View.GONE);
    }
}
