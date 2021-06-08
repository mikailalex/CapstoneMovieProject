package com.bumiayu.dicoding.favorite.movie

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumiayu.dicoding.capstonemovieproject.R
import com.bumiayu.dicoding.capstonemovieproject.core.domain.model.movie.MovieDetail
import com.bumiayu.dicoding.capstonemovieproject.core.utils.ImageUrl
import com.bumiayu.dicoding.capstonemovieproject.utils.GenreGenerator
import com.bumiayu.dicoding.favorite.databinding.ItemFavoriteBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class FavoriteMovieAdapter(private val onClickListener: IOnClickListener) :
    PagingDataAdapter<MovieDetail, FavoriteMovieAdapter.ViewHolder>(MOVIE_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = getItem(position)
        if (movie != null) {
            holder.bind(movie)
        }
    }

    inner class ViewHolder(private val binding: ItemFavoriteBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: MovieDetail) {
            with(binding) {
                tvTitle.text = movie.title
                tvGenres.text = GenreGenerator.create(movie.genres)
                tvDate.text = movie.releaseDate

                Glide.with(itemView.context)
                    .load(ImageUrl.poster(movie.imgPoster))
                    .apply(
                        RequestOptions.placeholderOf(R.drawable.ic_loading)
                            .error(R.drawable.ic_error)
                    )
                    .into(ivPoster)

                Glide.with(itemView.context)
                    .load(ImageUrl.poster(movie.imgBackground))
                    .apply(
                        RequestOptions.placeholderOf(R.drawable.ic_loading)
                            .error(R.drawable.ic_error)
                    )
                    .into(ivBackground)

                itemView.setOnClickListener { onClickListener.onClick(movie) }
            }
        }
    }

    companion object {
        private val MOVIE_COMPARATOR = object : DiffUtil.ItemCallback<MovieDetail>() {
            override fun areItemsTheSame(oldItem: MovieDetail, newItem: MovieDetail): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: MovieDetail, newItem: MovieDetail): Boolean {
                return oldItem == newItem
            }
        }
    }

    interface IOnClickListener {
        fun onClick(movie: MovieDetail)
    }
}
