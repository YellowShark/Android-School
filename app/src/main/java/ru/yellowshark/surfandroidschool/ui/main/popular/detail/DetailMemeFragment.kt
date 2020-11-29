package ru.yellowshark.surfandroidschool.ui.main.popular.detail

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.google.android.material.transition.MaterialContainerTransform
import org.koin.android.viewmodel.ext.android.viewModel
import ru.yellowshark.surfandroidschool.R
import ru.yellowshark.surfandroidschool.databinding.FragmentDetailMemeBinding
import ru.yellowshark.surfandroidschool.domain.meme.model.Meme
import ru.yellowshark.surfandroidschool.utils.JsonSerializer
import ru.yellowshark.surfandroidschool.utils.shareMeme
import ru.yellowshark.surfandroidschool.utils.viewBinding

class DetailMemeFragment :
    Fragment(R.layout.fragment_detail_meme),
    MenuItem.OnMenuItemClickListener {

    private val viewModel: DetailMemeViewModel by viewModel()
    private val binding: FragmentDetailMemeBinding by viewBinding(FragmentDetailMemeBinding::bind)
    private val args: DetailMemeFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = MaterialContainerTransform()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindData()
        initListeners()
    }

    private fun initListeners() {
        with(binding) {
            detailToolbar.setNavigationOnClickListener {
                fragmentManager?.popBackStack()
            }
            likeIv.setOnClickListener {
                isLiked = !isLiked!!
            }
            detailToolbar.menu.findItem(R.id.action_share).setOnMenuItemClickListener(this@DetailMemeFragment)
        }
    }

    private fun bindData() {
        with(binding) {
            val jsonMeme = args.jsonMeme
            if (jsonMeme.isNotEmpty()) {
                JsonSerializer.fromJson<Meme>(jsonMeme).let {
                    meme = it
                    isLiked = it.isFavorite
                    root.transitionName = it.photoUrl
                }
            }
            user = viewModel.getUserInfo()
        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.action_share)
            binding.meme?.let { meme -> activity?.shareMeme(meme) }
        return true
    }
}