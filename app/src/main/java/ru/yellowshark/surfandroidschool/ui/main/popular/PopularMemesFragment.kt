package ru.yellowshark.surfandroidschool.ui.main.popular

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import kotlinx.android.synthetic.main.fragment_popular_memes.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.yellowshark.surfandroidschool.R
import ru.yellowshark.surfandroidschool.data.network.MemesApi
import ru.yellowshark.surfandroidschool.data.network.popular.response.Meme

class PopularMemesFragment : Fragment(R.layout.fragment_popular_memes) {

    private val memesAdapter by lazy { MemesAdapter() }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initUi()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        MemesApi.getInstance().getPopularMemes().enqueue(object : Callback<List<Meme>> {
            override fun onResponse(call: Call<List<Meme>>, response: Response<List<Meme>>) {
                if (response.isSuccessful)
                    response.body()?.let { memesAdapter.setItems(it) }
                else
                    Toast.makeText(activity, R.string.error_fail_load_msg, Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(call: Call<List<Meme>>, t: Throwable) {}

        })

        /*GlobalScope.launch(Dispatchers.IO) {
            val response = MemesApi.getInstance().getPopularMemes()
            if (response.isSuccessful) {
                response.body()?.let { memesAdapter.setItems(it) }
            } else {
                Toast.makeText(activity, response.message(), Toast.LENGTH_SHORT).show()
            }
        }*/
    }

    private fun initUi() {
        val gridLayoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

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