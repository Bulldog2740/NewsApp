package com.yehor.newsapp.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.yehor.newsapp.R
import com.yehor.newsapp.databinding.ItemArticlePreviewBinding
import com.yehor.newsapp.data.model.Article

class NewsAdapter(
    private val navigateToArticle: ((Article) -> Unit)?
) : PagingDataAdapter<Article, NewsAdapter.ViewHolder>(diff) {
    companion object {
        var diff = object : DiffUtil.ItemCallback<Article>() {
            override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
                return oldItem.url == newItem.url
                        && oldItem.id == newItem.id
                        && oldItem.title == newItem.title
            }

            override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean =
                oldItem.title == newItem.title &&
                        oldItem.author == newItem.author &&
                        oldItem.content == newItem.content &&
                        oldItem.description == newItem.description &&
                        oldItem.publishedAt == newItem.publishedAt &&
                        oldItem.url == newItem.url
        }
    }

    class ViewHolder(private val binding: ItemArticlePreviewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(article: Article) {
            binding.article = article
            binding.executePendingBindings()
        }
    }

    fun getItemAt(position: Int): Article? = getItem(position)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding: ItemArticlePreviewBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.item_article_preview, parent, false
        )

        binding.root.setOnClickListener {
            binding.article?.let {
                navigateToArticle?.invoke(it)
            }
        }
        return ViewHolder(binding)
    }
}