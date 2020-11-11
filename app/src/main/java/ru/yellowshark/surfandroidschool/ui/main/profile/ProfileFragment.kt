package ru.yellowshark.surfandroidschool.ui.main.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import ru.yellowshark.surfandroidschool.R
import ru.yellowshark.surfandroidschool.databinding.FragmentProfileBinding
import ru.yellowshark.surfandroidschool.ui.main.popular.main.MemesAdapter
import ru.yellowshark.surfandroidschool.utils.BASE_USER_PHOTO

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val memesAdapter by lazy { MemesAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        bindData()
    }

    private fun bindData() {
        with(binding) {
            userPhoto = BASE_USER_PHOTO
            userName = "Daniil"
            userDescription = getString(R.string.short_dummy_text)
        }
    }

    private fun initRecyclerView() {
        with(binding) {
            val gridLayoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            memesAdapter.onItemClick = { meme ->
                /*val user = viewModel.getLastSessionUserInfo()
                val action =
                    PopularMemesFragmentDirections.actionOpenDetails(
                        gson.toJson(meme),
                        gson.toJson(user)
                    )
                view?.let { Navigation.findNavController(it).navigate(action) }*/
            }
            memesAdapter.onLikeClick = {
                Toast.makeText(activity, "${it.title} was liked", Toast.LENGTH_SHORT).show()
            }
            recyclerView.apply {
                layoutManager = gridLayoutManager
                adapter = memesAdapter
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}