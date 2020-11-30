package ru.yellowshark.surfandroidschool.ui.adapter

import android.view.View
import androidx.navigation.Navigation
import androidx.navigation.fragment.FragmentNavigatorExtras
import ru.yellowshark.surfandroidschool.R
import ru.yellowshark.surfandroidschool.domain.meme.model.Meme
import ru.yellowshark.surfandroidschool.ui.main.popular.main.PopularMemesFragmentDirections
import ru.yellowshark.surfandroidschool.utils.JsonSerializer

class AdapterFactory {
    companion object {
        fun getMemesAdapter(
            callingView: View?,
            onShare: ((Meme) -> Unit),
            onLike: ((Meme) -> Unit)? = null,
            ): MemesAdapter {
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
                onLikeClick = onLike
                onShareClick = onShare
            }
            return adapter
        }
    }
}