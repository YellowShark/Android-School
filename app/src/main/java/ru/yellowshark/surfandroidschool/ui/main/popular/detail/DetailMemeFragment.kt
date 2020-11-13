package ru.yellowshark.surfandroidschool.ui.main.popular.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import ru.yellowshark.surfandroidschool.R
import ru.yellowshark.surfandroidschool.databinding.FragmentDetailMemeBinding
import ru.yellowshark.surfandroidschool.domain.Meme
import ru.yellowshark.surfandroidschool.domain.User
import ru.yellowshark.surfandroidschool.utils.shareMeme

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
        initListeners()
    }

    private fun initListeners() {
        with(binding) {
            detailToolbar.setNavigationOnClickListener {
                fragmentManager?.popBackStack()
            }
            likeIv.setOnClickListener {
                meme?.isFavorite = !meme?.isFavorite!!
            }
            detailToolbar.menu.findItem(R.id.action_share).setOnMenuItemClickListener {
                meme?.let { meme -> context?.shareMeme(meme) }
                return@setOnMenuItemClickListener true
            }
        }
    }

    private fun bindData() {
        arguments?.let {
            with(binding) {
                val args = DetailMemeFragmentArgs.fromBundle(it)
                val jsonMeme = args.jsonMeme
                if (jsonMeme.isNotEmpty()) {
                    val memeFromJson = gson.fromJson(jsonMeme, Meme::class.java)
                    meme = memeFromJson
                }
                val jsonUser = args.jsonUser
                if (jsonUser.isNotEmpty()) {
                    val userFromJson = gson.fromJson(jsonUser, User::class.java)
                    user = userFromJson
                }
            }
        }
    }
}