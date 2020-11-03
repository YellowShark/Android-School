package ru.yellowshark.surfandroidschool.ui.main.popular

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import kotlinx.android.synthetic.main.fragment_popular_memes.*
import org.koin.android.viewmodel.ext.android.viewModel
import ru.yellowshark.surfandroidschool.R
import ru.yellowshark.surfandroidschool.data.network.auth.State
import ru.yellowshark.surfandroidschool.data.network.popular.response.Meme

class PopularMemesFragment : Fragment(R.layout.fragment_popular_memes) {

    private val viewModel: PopularMemesViewModel by viewModel()
    private val memesAdapter by lazy { MemesAdapter() }
    private val memesListObserver = Observer<State<List<Meme>>> { state ->
        when(state) {
            is State.Loading -> showLoading()
            is State.Success -> {
                memesAdapter.data = state.data
                progressBar.visibility = View.GONE
                memeList_rv.visibility = View.VISIBLE
                errorText_tv.visibility = View.GONE
            }
            is State.Error -> showError()
        }
    }

    private fun showError() {
        progressBar.visibility = View.GONE
        memeList_rv.visibility = View.GONE
        errorText_tv.visibility = View.VISIBLE
    }

    private fun showLoading() {
        progressBar.visibility = View.VISIBLE
        memeList_rv.visibility = View.GONE
        errorText_tv.visibility = View.GONE
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initUi()
        observeViewModel()
        updateList()
    }

    private fun updateList() {
        viewModel.requestPopularMemes()
    }

    private fun observeViewModel() {
        viewModel.memesListState.observe(viewLifecycleOwner, memesListObserver)
    }

    private fun initUi() {
        val gridLayoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        gridLayoutManager.gapStrategy
        memesAdapter.onItemClick = {
            Toast.makeText(activity, "${it.title} was clicked", Toast.LENGTH_SHORT).show()
        }
        memesAdapter.onLikeClick = {
            Toast.makeText(activity, "${it.title} was liked", Toast.LENGTH_SHORT).show()
        }

        memeList_rv.apply {
            layoutManager = gridLayoutManager
            adapter = memesAdapter
        }
    }
}