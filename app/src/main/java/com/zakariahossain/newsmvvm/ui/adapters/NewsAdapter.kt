package com.zakariahossain.newsmvvm.ui.adapters

import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import android.widget.ImageView
import android.widget.ProgressBar
import android.view.LayoutInflater
import com.zakariahossain.newsmvvm.R
import android.graphics.drawable.Drawable
import android.webkit.URLUtil
import com.bumptech.glide.load.DataSource
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.request.target.Target
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.RequestListener
import com.zakariahossain.newsmvvm.models.Article
import androidx.recyclerview.widget.AsyncListDiffer
import com.bumptech.glide.load.engine.GlideException
import kotlinx.android.synthetic.main.item_article_preview.view.*

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.ArticleViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder =
        ArticleViewHolder(
            LayoutInflater.from
                (parent.context).inflate(
                R.layout.item_article_preview,
                parent, false
            )
        )

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = differList.currentList[position]

        holder.itemView.apply {
            with(article) {
                tvTitle.text = title
                tvSource.text = source?.name
                tvDescription.text = description
                tvPublishedAt.text = publishedAt
            }

            loadImage(this, article?.urlToImage, ivArticleImage, progressArticleImage)
            setOnClickListener {
                onItemClickListener?.let { it(article) }
            }
        }
    }

    private fun loadImage(context: View, imageUrl: String?, imageView: ImageView, progressBar: ProgressBar) {
        if (imageUrl != null && URLUtil.isValidUrl(imageUrl)) {
            progressBar.visibility = View.VISIBLE

            Glide.with(context)
                .load(imageUrl)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?, model: Any?, target:
                        Target<Drawable>?, isFirstResource: Boolean
                    ): Boolean {
                        progressBar.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?, model: Any?, target:
                        Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean
                    ): Boolean {
                        progressBar.visibility = View.GONE
                        return false
                    }
                }).into(imageView)
        }
    }

    override fun getItemCount(): Int = differList.currentList.size

    private val differCallback = object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }

    val differList = AsyncListDiffer(this, differCallback)

    inner class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private var onItemClickListener: ((Article) -> Unit)? = null

    fun setOnItemClickListener(listener: (Article) -> Unit) {
        onItemClickListener = listener
    }
}