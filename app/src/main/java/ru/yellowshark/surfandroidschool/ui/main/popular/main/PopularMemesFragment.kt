package ru.yellowshark.surfandroidschool.ui.main.popular.main

import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import android.view.View
import androidx.navigation.Navigation
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import org.koin.android.viewmodel.ext.android.viewModel
import ru.yellowshark.surfandroidschool.R
import ru.yellowshark.surfandroidschool.databinding.FragmentPopularMemesBinding
import ru.yellowshark.surfandroidschool.domain.ResponseError
import ru.yellowshark.surfandroidschool.domain.meme.model.Meme
import ru.yellowshark.surfandroidschool.ui.adapter.AdapterFactory
import ru.yellowshark.surfandroidschool.ui.adapter.MemesAdapter
import ru.yellowshark.surfandroidschool.ui.base.BaseFragment
import ru.yellowshark.surfandroidschool.utils.shareMeme
import ru.yellowshark.surfandroidschool.utils.viewBinding

class PopularMemesFragment :
    BaseFragment(R.layout.fragment_popular_memes),
    SwipeRefreshLayout.OnRefreshListener,
    MenuItem.OnMenuItemClickListener {

    private val viewModel: PopularMemesViewModel by viewModel()
    private val binding: FragmentPopularMemesBinding by viewBinding(FragmentPopularMemesBinding::bind)
    private lateinit var memesAdapter: MemesAdapter

    override fun showContent(data: List<Meme>?) {
        with(binding) {
            data?.let { memesAdapter.data = it }
            progressBar.root.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
            errorTextTv.visibility = View.GONE
        }
    }

    override fun showError(error: ResponseError) {
        with(binding) {
            progressBar.root.visibility = View.GONE
            recyclerView.visibility = View.GONE
            errorTextTv.visibility = View.VISIBLE
            errorTextTv.text = getErrorMessageText(error)
        }
    }

    override fun showLoading() {
        with(binding) {
            progressBar.root.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
            errorTextTv.visibility = View.GONE
        }
    }

    override fun doFinallyAfterEvent() {
        hideRefresher()
    }

    private fun hideRefresher() {
        with(binding.refresherSrl) {
            if (isRefreshing)
                isRefreshing = false
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
        observeViewModel()
    }

    private fun observeViewModel() {
        with(viewModel) {
            memesListViewState.observe(viewLifecycleOwner, viewStateObserver)
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
        memesAdapter = AdapterFactory.getMemesAdapter(
            callingView = view,
            onShare = { meme -> activity?.shareMeme(meme) }
        )
        binding.recyclerView.apply {
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

    private fun updateList() {
        with(viewModel) {
            requestPopularMemes()
        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.action_search) {
            val action = PopularMemesFragmentDirections.actionFilterSearch()
            view?.let { Navigation.findNavController(it).navigate(action) }
        }
        return true
    }
}