package ru.yellowshark.surfandroidschool.ui.main.popular.main

import android.app.Activity
import android.view.View
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.fragment.FragmentNavigatorExtras
import ru.yellowshark.surfandroidschool.R
import ru.yellowshark.surfandroidschool.utils.JsonSerializer
import ru.yellowshark.surfandroidschool.utils.shareMeme

class AdapterFactory {
    companion object {
        fun getMemesAdapter(callingView: View?, activity: Activity?): MemesAdapter {
            val adapter = MemesAdapter()
            adapter.apply {
                onItemClick = { meme, itemView ->
                    val extras =
                        FragmentNavigatorExtras(sharedElements = arrayOf(itemView to meme.photoUrl))
                    val action =
                        PopularMemesFragmentDirections.actionOpenDetails(JsonSerializer.toJson(meme))
                    callingView?.let {
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
}