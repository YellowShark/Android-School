package ru.yellowshark.surfandroidschool.ui.main.popular.detail

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.google.android.material.transition.MaterialContainerTransform
import ru.yellowshark.surfandroidschool.R
import ru.yellowshark.surfandroidschool.databinding.FragmentDetailMemeBinding
import ru.yellowshark.surfandroidschool.domain.Meme
import ru.yellowshark.surfandroidschool.domain.User
import ru.yellowshark.surfandroidschool.utils.SingleGson
import ru.yellowshark.surfandroidschool.utils.shareMeme
import ru.yellowshark.surfandroidschool.utils.viewBinding

class DetailMemeFragment :
    Fragment(R.layout.fragment_detail_meme),
    MenuItem.OnMenuItemClickListener {

    private val binding: FragmentDetailMemeBinding by viewBinding(FragmentDetailMemeBinding::bind)
    private val gson by lazy { SingleGson.getInstance() }
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
                val memeFromJson = gson.fromJson(jsonMeme, Meme::class.java)
                meme = memeFromJson
                isLiked = memeFromJson.isFavorite
                root.transitionName = memeFromJson.photoUrl
            }
            val jsonUser = args.jsonUser
            if (jsonUser.isNotEmpty()) {
                val userFromJson = gson.fromJson(jsonUser, User::class.java)
                user = userFromJson
            }
        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.action_share)
            binding.meme?.let { meme -> activity?.shareMeme(meme) }
        return true
    }
}