package ru.yellowshark.surfandroidschool.ui.main.popular.search

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import org.koin.android.viewmodel.ext.android.viewModel
import ru.yellowshark.surfandroidschool.R
import ru.yellowshark.surfandroidschool.databinding.FragmentSearchFilterBinding
import ru.yellowshark.surfandroidschool.domain.ResponseError
import ru.yellowshark.surfandroidschool.domain.meme.model.Meme
import ru.yellowshark.surfandroidschool.ui.adapter.AdapterFactory
import ru.yellowshark.surfandroidschool.ui.adapter.MemesAdapter
import ru.yellowshark.surfandroidschool.ui.base.BaseFragment
import ru.yellowshark.surfandroidschool.utils.MIN_QUERY_LENGTH
import ru.yellowshark.surfandroidschool.utils.shareMeme
import ru.yellowshark.surfandroidschool.utils.viewBinding

class MemesSearchFilterFragment : BaseFragment(R.layout.fragment_search_filter) {
    private val viewModel: MemeSearchFilterViewModel by viewModel()
    private val binding: FragmentSearchFilterBinding by viewBinding(FragmentSearchFilterBinding::bind)
    private lateinit var searchView: SearchView
    private lateinit var searchItem: MenuItem
    private lateinit var memesAdapter: MemesAdapter

    override fun showError(error: ResponseError) {
        with(binding) {
            recyclerView.visibility = View.GONE
            placeholderTv.visibility = View.VISIBLE
        }
    }

    override fun showContent(data: List<Meme>?) {
        with(binding) {
            data?.let { memesAdapter.data = it }
            recyclerView.visibility = View.VISIBLE
            placeholderTv.visibility = View.GONE
        }
    }

    override fun showLoading() {
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
        observeViewModel()
    }

    private fun observeViewModel() {
        with(viewModel) {
            filterViewState.observe(viewLifecycleOwner, viewStateObserver)
        }
    }

    private fun initUi() {
        initListeners()
        initSearchView()
        initRecyclerView()
    }

    private fun initListeners() {
        binding.toolbar.setNavigationOnClickListener { fragmentManager?.popBackStack() }
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

    private fun initSearchView() {
        searchItem = binding.toolbar.menu.findItem(R.id.action_search)
        searchView = searchItem.actionView as SearchView
        with(searchView) {
            focusable = SearchView.FOCUSABLE
            isIconified = false
            queryHint = getString(R.string.search)
            setOnQueryTextListener(searchTextListener)
            setOnCloseListener {
                memesAdapter.data = emptyList()
                searchItem.collapseActionView()
            }
        }
    }

    private val searchTextListener = object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            with(searchView) {
                clearFocus()
                searchItem.collapseActionView()
                if (query != null && query.length > MIN_QUERY_LENGTH) {
                    viewModel.searchMemes(query)
                }
                setQuery("", false)
            }
            return true
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            if (newText != null && newText.length >= 2) {
                viewModel.searchMemes(newText)
            }
            return true
        }
    }
}