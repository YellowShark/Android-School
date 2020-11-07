package ru.yellowshark.surfandroidschool.ui.main.popular.main

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.gson.Gson
import org.koin.android.viewmodel.ext.android.viewModel
import ru.yellowshark.surfandroidschool.R
import ru.yellowshark.surfandroidschool.data.db.entity.Meme
import ru.yellowshark.surfandroidschool.data.network.auth.State
import ru.yellowshark.surfandroidschool.databinding.FragmentPopularMemesBinding

class PopularMemesFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    private val viewModel: PopularMemesViewModel by viewModel()
    private var _binding: FragmentPopularMemesBinding? = null
    private val binding get() = _binding!!
    private val gson = Gson()
    private val memesAdapter by lazy { MemesAdapter() }
    private val memesListObserver = Observer<State<List<Meme>>> { state ->
        when(state) {
            is State.Loading -> showLoading()
            is State.Success -> {
                memesAdapter.data = state.data
                showContent()
            }
            is State.Error -> showError()
        }
        hideRefresher()
    }

    private fun hideRefresher() {
        with(binding.refresherSrl) {
            if (isRefreshing)
                isRefreshing = false
        }
    }

    private fun showContent() {
        with(binding) {
            progressBar.root.visibility = View.GONE
            memeListRv.visibility = View.VISIBLE
            errorTextTv.visibility = View.GONE
        }
    }

    private fun showError() {
        with(binding) {
            progressBar.root.visibility = View.GONE
            memeListRv.visibility = View.GONE
            errorTextTv.visibility = View.VISIBLE
        }
    }

    private fun showLoading() {
        with(binding) {
            progressBar.root.visibility = View.VISIBLE
            memeListRv.visibility = View.GONE
            errorTextTv.visibility = View.GONE
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPopularMemesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
        observeViewModel()
        updateList()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun updateList() {
        viewModel.requestPopularMemes()
    }

    private fun observeViewModel() {
        viewModel.memesListState.observe(viewLifecycleOwner, memesListObserver)
    }

    private fun initUi() {
        initRefresher()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        val gridLayoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        memesAdapter.onItemClick = { meme ->
            val user = viewModel.getLastSessionUserInfo()
            val action =
                PopularMemesFragmentDirections.actionOpenDetails(
                    gson.toJson(meme),
                    gson.toJson(user)
                )
            view?.let { Navigation.findNavController(it).navigate(action) }
        }
        memesAdapter.onLikeClick = {
            Toast.makeText(activity, "${it.title} was liked", Toast.LENGTH_SHORT).show()
        }

        binding.memeListRv.apply {
            layoutManager = gridLayoutManager
            adapter = memesAdapter
        }
    }

    private fun initRefresher() {
        binding.refresherSrl.apply {
            setOnRefreshListener(this@PopularMemesFragment)
            setColorSchemeResources(R.color.ebony_clay)
            setProgressBackgroundColorSchemeResource(R.color.bright_turquoise)
        }
    }

    override fun onRefresh() {
        Handler().postDelayed({
            updateList()
        }, 500)
    }
}