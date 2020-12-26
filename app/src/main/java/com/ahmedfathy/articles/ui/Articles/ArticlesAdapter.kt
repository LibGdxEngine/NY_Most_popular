package com.ahmedfathy.articles.ui.Articles

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ahmedfathy.articles.data.ArticleEntity
import com.ahmedfathy.articles.databinding.ArticleRowBinding

class ArticlesAdapter(private val listener: OnItemClickListener) :
    ListAdapter<ArticleEntity, ArticlesAdapter.TasksViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasksViewHolder {
        val binding = ArticleRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TasksViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TasksViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    inner class TasksViewHolder(private val binding: ArticleRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
                root.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val task = getItem(position)
                        listener.onItemClick(task)
                    }
                }
            }
        }

        fun bind(articleEntity: ArticleEntity) {
            binding.apply {
                textViewTitle.text = articleEntity.title
                textViewSubtitle.text = articleEntity.source
                textViewDate.text = articleEntity.publishedDate
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(articleEntity: ArticleEntity)
    }

    class DiffCallback : DiffUtil.ItemCallback<ArticleEntity>() {
        override fun areItemsTheSame(oldItem: ArticleEntity, newItem: ArticleEntity) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: ArticleEntity, newItem: ArticleEntity) =
            oldItem == newItem
    }
}