package com.bumiayu.dicoding.favorite.tv

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumiayu.dicoding.capstonemovieproject.R
import com.bumiayu.dicoding.capstonemovieproject.core.domain.model.tvshow.TvShowDetail
import com.bumiayu.dicoding.capstonemovieproject.core.utils.ImageUrl
import com.bumiayu.dicoding.capstonemovieproject.utils.GenreGenerator
import com.bumiayu.dicoding.favorite.databinding.ItemFavoriteBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class FavoriteTvShowAdapter(private val onClickListener: IOnClickListener) : PagingDataAdapter<TvShowDetail, FavoriteTvShowAdapter.ViewHolder>(
    MOVIE_COMPARATOR
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tvShow = getItem(position)
        if (tvShow != null) {
            holder.bind(tvShow)
        }
    }

    inner class ViewHolder(private val binding: ItemFavoriteBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(tvShow: TvShowDetail) {
            with(binding) {
                tvTitle.text = tvShow.title
                tvGenres.text = GenreGenerator.create(tvShow.genres)
                tvDate.text = tvShow.releaseDate

                Glide.with(itemView.context)
                    .load(ImageUrl.poster(tvShow.imgPoster))
                    .apply(
                        RequestOptions.placeholderOf(R.drawable.ic_loading)
                            .error(R.drawable.ic_error)
                    )
                    .into(ivPoster)

                Glide.with(itemView.context)
                    .load(ImageUrl.poster(tvShow.imgBackground))
                    .apply(
                        RequestOptions.placeholderOf(R.drawable.ic_loading)
                            .error(R.drawable.ic_error)
                    )
                    .into(ivBackground)

                itemView.setOnClickListener { onClickListener.onClick(tvShow) }
            }
        }
    }

    companion object {
        private val MOVIE_COMPARATOR = object : DiffUtil.ItemCallback<TvShowDetail>() {
            override fun areItemsTheSame(oldItem: TvShowDetail, newItem: TvShowDetail): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: TvShowDetail, newItem: TvShowDetail): Boolean {
                return oldItem == newItem
            }
        }
    }

    interface IOnClickListener {
        fun onClick(tvShow: TvShowDetail)
    }
}
