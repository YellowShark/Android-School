package ru.yellowshark.surfandroidschool.ui.main.popular

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_meme.view.*
import ru.yellowshark.surfandroidschool.R
import ru.yellowshark.surfandroidschool.data.network.popular.response.Meme
import ru.yellowshark.surfandroidschool.databinding.ItemMemeBinding

class MemesAdapter : RecyclerView.Adapter<MemesAdapter.MemeViewHolder>() {

    var data: List<Meme> = emptyList()
    var onItemClick: ((Meme) -> Unit)? = null
    var onLikeClick: ((Meme) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MemeViewHolder.create(parent)

    override fun onBindViewHolder(holder: MemeViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
        with(holder) {
            bind(item)
            itemView.setOnClickListener { onItemClick?.let { click -> click(item) } }
            itemView.like_iv.setOnClickListener {
                item.isFavorite = !item.isFavorite
                updateLike(isLiked = item.isFavorite)
                onLikeClick?.let { click -> click(item) }
            }
        }
    }

    override fun getItemCount() = data.size

    fun setItems(list: List<Meme>) {
        data = list
        notifyDataSetChanged()
    }

    class MemeViewHolder(private val binding: ItemMemeBinding) : RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun create(parent: ViewGroup): MemeViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding: ItemMemeBinding =
                    DataBindingUtil.inflate(inflater, R.layout.item_meme, parent, false)
                return MemeViewHolder(binding)
            }
        }

        fun bind(meme: Meme) {
            with(binding) {
                pictureUrl = meme.photoUrl
                title = meme.title
                isLiked = meme.isFavorite
                executePendingBindings()
            }
        }

        fun updateLike(isLiked: Boolean) {
            binding.isLiked = isLiked
            binding.executePendingBindings()
        }

    }
}