package com.ahmedfathy.articles.ui.Articles

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ahmedfathy.articles.R
import com.ahmedfathy.articles.data.ArticleEntity
import com.ahmedfathy.articles.databinding.ArticleRowBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

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
                //mark already completed article in purple color to recognize it as completed
                if(articleEntity.completed){
                    textViewTitle.setTextColor(rootCardView.context.resources.getColor(R.color.completed_articles_color))
                    textViewSubtitle.setTextColor(rootCardView.context.resources.getColor(R.color.completed_articles_color))
                    textViewDate.setTextColor(rootCardView.context.resources.getColor(R.color.completed_articles_color))
                    sectionsTextView.setTextColor(rootCardView.context.resources.getColor(R.color.completed_articles_color))
                }
                textViewTitle.text = articleEntity.title
                textViewSubtitle.text = articleEntity.source
                textViewDate.text = articleEntity.publishedDate
                sectionsTextView.text = articleEntity.section
                Glide.with(articleImageview.context)
                    .load(articleEntity.thumbnail)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.logo_)
                    .into(articleImageview)
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