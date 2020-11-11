package ru.yellowshark.surfandroidschool.ui.main.popular.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import ru.yellowshark.surfandroidschool.R
import ru.yellowshark.surfandroidschool.data.db.entity.EntityMeme
import ru.yellowshark.surfandroidschool.databinding.FragmentDetailMemeBinding
import ru.yellowshark.surfandroidschool.domain.User
import ru.yellowshark.surfandroidschool.utils.BASE_USER_PHOTO

class DetailMemeFragment : Fragment() {

    private var _binding: FragmentDetailMemeBinding? = null
    private val binding get() = _binding!!
    private val gson = Gson()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_detail_meme, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindData()
        initView()
    }

    private fun initView() {
        with(binding) {
            detailToolbar.setNavigationOnClickListener {
                fragmentManager?.popBackStack()
            }
            likeIv.setOnClickListener {
                isLiked = !isLiked!!
            }
        }
    }

    private fun bindData() {
        arguments?.let {
            with(binding) {
                val args = DetailMemeFragmentArgs.fromBundle(it)
                val jsonMeme = args.jsonMeme
                val jsonUser = args.jsonUser
                if (jsonMeme.isNotEmpty()) {
                    val meme = gson.fromJson(jsonMeme, EntityMeme::class.java)
                    photoUrl = meme.photoUrl
                    isLiked = meme.isFavorite
                    memeTitle = meme.title
                    description = meme.description
                    date = meme.createdDate
                }
                if (jsonUser.isNotEmpty()) {
                    val userInfo = gson.fromJson(jsonUser, User::class.java)
                    userName = userInfo.firstName
                    userPhoto = BASE_USER_PHOTO
                }
            }
        }
    }
}