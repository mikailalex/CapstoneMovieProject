package com.bumiayu.dicoding.capstonemovieproject.ui.movie

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumiayu.dicoding.capstonemovieproject.R
import com.bumiayu.dicoding.capstonemovieproject.core.domain.model.movie.Movie
import com.bumiayu.dicoding.capstonemovieproject.core.utils.ImageUrl
import com.bumiayu.dicoding.capstonemovieproject.databinding.ItemGridBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class MovieAdapter(private val onClickListener: IOnClickListener) : PagingDataAdapter<Movie, MovieAdapter.MovieViewHolder>(MOVIE_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemGridBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = getItem(position)
        if (movie != null) {
            holder.bind(movie)
        }
    }

    inner class MovieViewHolder(private val binding: ItemGridBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: Movie) {
            with(binding) {
                tvTitle.text = movie.title
                tvScore.text = movie.score.toString()

                Glide.with(itemView.context)
                    .load(ImageUrl.poster(movie.imgPoster))
                    .apply(
                        RequestOptions.placeholderOf(R.drawable.ic_loading)
                            .error(R.drawable.ic_error)
                    )
                    .into(imgItemPhoto)

                itemView.setOnClickListener { onClickListener.onClick(movie) }
            }
        }
    }

    companion object {
        private val MOVIE_COMPARATOR = object : DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem == newItem
            }
        }
    }

    interface IOnClickListener {
        fun onClick(movie: Movie)
    }
}
