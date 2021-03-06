package ru.yellowshark.surfandroidschool.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_meme.view.*
import ru.yellowshark.surfandroidschool.R
import ru.yellowshark.surfandroidschool.databinding.ItemMemeBinding
import ru.yellowshark.surfandroidschool.domain.meme.model.Meme

class MemesAdapter : RecyclerView.Adapter<MemesAdapter.MemeViewHolder>() {
    var onItemClick: ((Meme, View) -> Unit)? = null
    var onLikeClick: ((Meme) -> Unit)? = null
    var onShareClick: ((Meme) -> Unit)? = null
    var data: List<Meme> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        MemeViewHolder.create(parent)

    override fun onBindViewHolder(holder: MemeViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
        with(holder.itemView) {
            setOnClickListener {
                onItemClick?.let { click -> click(item, holder.itemView) }
            }
            like_iv.setOnClickListener {
                item.isFavorite = !item.isFavorite
                holder.updateLike(isLiked = item.isFavorite)
                onLikeClick?.let { click -> click(item) }
            }
            share_iv.setOnClickListener { onShareClick?.let { click -> click(item) } }
        }
    }

    override fun getItemCount() = data.size

    class MemeViewHolder(private val binding: ItemMemeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(meme: Meme) {
            with(binding) {
                binding.root.transitionName = meme.photoUrl
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

        companion object {
            fun create(parent: ViewGroup): MemeViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding: ItemMemeBinding =
                    DataBindingUtil.inflate(inflater, R.layout.item_meme, parent, false)
                return MemeViewHolder(binding)
            }
        }
    }
}