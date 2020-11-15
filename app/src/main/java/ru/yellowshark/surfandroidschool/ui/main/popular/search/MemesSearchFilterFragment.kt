package ru.yellowshark.surfandroidschool.ui.main.popular.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_search_filter.*
import org.koin.android.viewmodel.ext.android.viewModel
import ru.yellowshark.surfandroidschool.R
import ru.yellowshark.surfandroidschool.databinding.FragmentSearchFilterBinding
import ru.yellowshark.surfandroidschool.domain.ViewState
import ru.yellowshark.surfandroidschool.ui.main.popular.main.MemesAdapter
import ru.yellowshark.surfandroidschool.ui.main.popular.main.PopularMemesFragmentDirections
import ru.yellowshark.surfandroidschool.utils.shareMeme

class MemesSearchFilterFragment : Fragment() {
    private var _binding: FragmentSearchFilterBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MemeSearchFilterViewModel by viewModel()
    private lateinit var searchView: SearchView
    private lateinit var searchItem: MenuItem
    private val memesAdapter = MemesAdapter()
    private val gson by lazy { Gson() }
    private val viewStateObserver = Observer<ViewState> { state ->
        when (state) {
            is ViewState.Success -> showResults()
            is ViewState.Error -> showError()
        }
    }

    private fun showError() {
        with(binding) {
            recyclerView.visibility = View.GONE
            placeholderTv.visibility = View.VISIBLE
        }
    }

    private fun showResults() {
        with(binding) {
            recyclerView.visibility = View.VISIBLE
            placeholderTv.visibility = View.GONE
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchFilterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
        observeViewModel()
    }

    private fun observeViewModel() {
        with(viewModel) {
            filterViewState.observe(viewLifecycleOwner, viewStateObserver)
            filteredMemesLiveData.observe(viewLifecycleOwner) {
                memesAdapter.data = it
            }
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
        memesAdapter.apply {
            onItemClick = { meme, itemView ->
                val user = viewModel.getLastSessionUserInfo()
                val extras = FragmentNavigatorExtras(itemView to meme.photoUrl)
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
        recyclerView.apply {
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private val searchTextListener = object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            with(searchView) {
                clearFocus()
                searchItem.collapseActionView()
                if (query != null && query.length > 2) {
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