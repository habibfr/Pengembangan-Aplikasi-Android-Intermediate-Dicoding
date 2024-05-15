package com.habibfr.mystoryapp.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.habibfr.mystoryapp.data.entity.ListStoryItem
import com.habibfr.mystoryapp.databinding.ItemStoryBinding


class StoryAdapter :
    PagingDataAdapter<ListStoryItem, StoryAdapter.MyViewHolder>(DIFF_CALLBACK) {
    private lateinit var onItemClickCallback: OnItemClickCallback

    class MyViewHolder(val binding: ItemStoryBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(storyItem: ListStoryItem) {
            Glide.with(itemView.context).load(storyItem.photoUrl)
                .into(binding.storyImage)
            binding.storyTitle.text = storyItem.name

            val description: String? = storyItem.description?.let { truncateDescription(it) }

            binding.storyDescription.text = description
        }

        private fun truncateDescription(description: String): String {
            val words = description.split(" ")
            return if (words.size <= 10) {
                description
            } else {
                words.subList(0, 10).joinToString(" ") + "..."
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val storyItem = getItem(position)
        holder.itemView.setOnClickListener {
            if (storyItem != null) {
                onItemClickCallback.onItemClicked(storyItem)
            }
        }

        if (storyItem != null) {
            holder.bind(storyItem)
        }
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(storyItem: ListStoryItem)
    }


    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(
                oldItem: ListStoryItem, newItem: ListStoryItem
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ListStoryItem, newItem: ListStoryItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

}