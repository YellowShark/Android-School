package ru.yellowshark.surfandroidschool.ui.main.popular.main

import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.gson.Gson
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import ru.yellowshark.surfandroidschool.R
import ru.yellowshark.surfandroidschool.databinding.FragmentPopularMemesBinding
import ru.yellowshark.surfandroidschool.domain.ViewState
import ru.yellowshark.surfandroidschool.utils.shareMeme
import ru.yellowshark.surfandroidschool.utils.viewBinding

class PopularMemesFragment :
    Fragment(R.layout.fragment_popular_memes),
    SwipeRefreshLayout.OnRefreshListener,
    MenuItem.OnMenuItemClickListener {

    private val viewModel: PopularMemesViewModel by viewModel()
    private val binding: FragmentPopularMemesBinding by viewBinding(FragmentPopularMemesBinding::bind)
    private val gson: Gson by inject()
    private val memesAdapter = MemesAdapter()
    private val viewStateObserver = Observer<ViewState> { state ->
        when (state) {
            is ViewState.Loading -> showLoading()
            is ViewState.Success -> showContent()
            is ViewState.Error -> showError(state.msg)
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

    private fun showError(msg: String?) {
        with(binding) {
            progressBar.root.visibility = View.GONE
            memeListRv.visibility = View.GONE
            errorTextTv.visibility = View.VISIBLE
            errorTextTv.text = msg ?: getString(R.string.error_fail_load_msg)
        }
    }

    private fun showLoading() {
        with(binding) {
            progressBar.root.visibility = View.VISIBLE
            memeListRv.visibility = View.GONE
            errorTextTv.visibility = View.GONE
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
        observeViewModel()
    }

    private fun updateList() {
        with(viewModel) {
            requestPopularMemes()
        }
    }

    private fun observeViewModel() {
        with(viewModel) {
            memesListViewState.observe(viewLifecycleOwner, viewStateObserver)
            memesLiveData.observe(viewLifecycleOwner, {
                memesAdapter.data = it
            })
        }
    }

    private fun initUi() {
        initListeners()
        initRefresher()
        initRecyclerView()
    }

    private fun initListeners() {
        binding.searchToolbar.menu.findItem(R.id.action_search).setOnMenuItemClickListener(this)
    }

    private fun initRecyclerView() {
        val gridLayoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        memesAdapter.apply {
            onItemClick = { meme, itemView ->
                val extras = FragmentNavigatorExtras(itemView to meme.photoUrl)
                val user = viewModel.getLastSessionUserInfo()
                val action =
                    PopularMemesFragmentDirections.actionOpenDetails(
                        gson.toJson(meme),
                        gson.toJson(user)
                    )
                view?.let {
                    Navigation.findNavController(it)
                        .navigate(R.id.destination_meme_detail, action.arguments, null, extras)
                }
            }
            onLikeClick = {
                Toast.makeText(activity, "${it.title} was liked", Toast.LENGTH_SHORT).show()
            }
            onShareClick = { meme ->
                activity?.shareMeme(meme)
            }
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

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.action_search) {
            val action = PopularMemesFragmentDirections.actionFilterSearch()
            view?.let { Navigation.findNavController(it).navigate(action) }
        }
        return true
    }
}