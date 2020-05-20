package `in`.codeshuffle.examplelinkpreview.ui.fragment.demoscreen

import `in`.codeshuffle.examplelinkpreview.databinding.FragmentDemoBinding
import `in`.codeshuffle.linkpreviewedittext.LinkInfo
import `in`.codeshuffle.linkpreviewedittext.listener.LinkPreviewListener
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

class DemoFragment : Fragment(), LinkPreviewListener {

    companion object {
        private val TAG = DemoFragment::class.java.simpleName

        fun getInstance(): DemoFragment {
            val bundle = Bundle()
            val demoFragment = DemoFragment()
            demoFragment.arguments = bundle
            return demoFragment
        }
    }

    private lateinit var binding: FragmentDemoBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentDemoBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.linkPreviewEditText.detectLinksWhileTyping(true)
        binding.linkPreviewLayout.closePreview.setOnClickListener { v: View? -> binding.linkPreviewEditText.closePreview() }
        binding.linkPreviewEditText.setLinkPreviewListener(this)
        binding.linkPreviewEditText.setCacheEnabled(true)
    }

    override fun onLinkFound(url: String) {
        Log.d(TAG, "onLinkFound: $url")
    }

    override fun onLinkPreviewFound(linkInfo: LinkInfo?, fromCache: Boolean) {
        activity?.let {
            linkInfo?.run {
                Log.d(TAG, "onLinkPreviewFound: $linkInfo from cache=$fromCache")
                binding.linkPreviewLayout.linkTitle.text = linkInfo.title
                binding.linkPreviewLayout.linkDesc.text = linkInfo.description
                binding.linkPreviewLayout.linkUrl.text = linkInfo.url
                binding.linkPreviewLayout.linkDesc.visibility = if (linkInfo.description?.isEmpty()?:false) View.GONE else View.VISIBLE
                Glide.with(it)
                        .load(linkInfo.imageUrl)
                        .addListener(object : RequestListener<Drawable?> {
                            override fun onLoadFailed(e: GlideException?, model: Any, target: Target<Drawable?>, isFirstResource: Boolean): Boolean {
                                binding.linkPreviewLayout.linkImage.visibility = View.GONE
                                binding.linkPreviewLayout.root.visibility = View.VISIBLE
                                return false
                            }

                            override fun onResourceReady(resource: Drawable?, model: Any, target: Target<Drawable?>, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                                binding.linkPreviewLayout.linkImage.visibility = View.VISIBLE
                                binding.linkPreviewLayout.root.visibility = View.VISIBLE
                                return false
                            }
                        })
                        .into(binding.linkPreviewLayout.linkImage)
            }
        }
    }

    override fun onNoLinkPreview() {
        Log.d(TAG, "onNoLinkPreview: Closed")
        binding.linkPreviewLayout.root.visibility = View.GONE
    }

    override fun onLinkPreviewError(errorMsg: String) {
        Log.d(TAG, "onLinkPreviewError: $errorMsg")
        binding.linkPreviewLayout.root.visibility = View.GONE
    }
}