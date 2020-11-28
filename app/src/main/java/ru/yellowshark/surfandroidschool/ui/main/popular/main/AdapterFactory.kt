package ru.yellowshark.surfandroidschool.ui.main.popular.main

import android.app.Activity
import android.view.View
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.fragment.FragmentNavigatorExtras
import com.google.gson.Gson
import ru.yellowshark.surfandroidschool.R
import ru.yellowshark.surfandroidschool.domain.user.model.User
import ru.yellowshark.surfandroidschool.utils.shareMeme

object AdapterFactory {
    private val gson = Gson()
    fun getMemesAdapter(activity: Activity?, view: View?, user: User): MemesAdapter {
        val adapter = MemesAdapter()
        adapter.apply {
            onItemClick = { meme, itemView ->
                val extras =
                    FragmentNavigatorExtras(sharedElements = arrayOf(itemView to meme.photoUrl))
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
            onShareClick = { meme -> activity?.shareMeme(meme) }
        }
        return adapter
    }
}