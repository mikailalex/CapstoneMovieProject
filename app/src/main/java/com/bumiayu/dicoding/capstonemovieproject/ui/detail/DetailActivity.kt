package com.bumiayu.dicoding.capstonemovieproject.ui.detail

import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.bumiayu.dicoding.capstonemovieproject.R
import com.bumiayu.dicoding.capstonemovieproject.core.domain.model.Resource
import com.bumiayu.dicoding.capstonemovieproject.core.domain.model.movie.MovieDetail
import com.bumiayu.dicoding.capstonemovieproject.core.domain.model.tvshow.TvShowDetail
import com.bumiayu.dicoding.capstonemovieproject.core.ui.BaseActivity
import com.bumiayu.dicoding.capstonemovieproject.core.utils.ImageUrl
import com.bumiayu.dicoding.capstonemovieproject.databinding.ActivityDetailBinding
import com.bumiayu.dicoding.capstonemovieproject.ui.movie.MovieViewModel
import com.bumiayu.dicoding.capstonemovieproject.ui.tv.TvShowViewModel
import com.bumiayu.dicoding.capstonemovieproject.utils.GenreGenerator
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailActivity : BaseActivity<ActivityDetailBinding>({ ActivityDetailBinding.inflate(it) }) {

    companion object {
        const val EXTRA_ID = "extra_id"
        const val EXTRA_CATEGORY = "extra_category"
    }

    private val movieViewModel: MovieViewModel by viewModel()
    private val tvShowViewModel: TvShowViewModel by viewModel()
    private lateinit var category: String
    private var id = 0

    override fun ActivityDetailBinding.onCreate(savedInstanceState: Bundle?) {

        binding.toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

        id = intent.getIntExtra(EXTRA_ID, 0)
        category = intent.getStringExtra(EXTRA_CATEGORY).toString()

        if (category == "movie") setMovie() else setTvShow()
        binding.fabAddToFavorite.setOnClickListener { onClickFavorite() }
    }

    private fun setMovie() {
        lifecycleScope.launch {
            movieViewModel.getDetailsMovie(id).collectLatest { movie ->
                when (movie) {
                    is Resource.Loading -> loadingState(true)
                    is Resource.Success -> {
                        loadingState(true)
                        loadDataMovie(movie.data)
                        movie.data?.let { movieViewModel.setMovie(it) }
                        setFavoriteState(movie.data?.isFavorite)
                    }
                    is Resource.Error -> {
                        loadingState(false)
                        Toast.makeText(
                            this@DetailActivity,
                            "Gagal memuat data\n${movie.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    else -> Toast.makeText(
                        this@DetailActivity,
                        "Sesuatu Bermasalah",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun loadDataMovie(item: MovieDetail?) {
        if (item != null) {
            val scoreGenreReleaseDate = resources.getString(
                R.string.score_genre_release_date,
                item.score.toString(),
                GenreGenerator.create(item.genres),
                item.releaseDate
            )
            with(binding) {
                collapsing.title = item.title
                tvDescDetail.text = item.description
                tvScoreGenreReleaseDate.text = scoreGenreReleaseDate

                Glide.with(this@DetailActivity)
                    .load(ImageUrl.posterBig(item.imgPoster))
                    .apply(
                        RequestOptions.placeholderOf(R.drawable.ic_loading)
                            .error(R.drawable.ic_error)
                    )
                    .into(imgItemPhoto)
                Glide.with(this@DetailActivity)
                    .load(ImageUrl.background(item.imgBackground))
                    .apply(
                        RequestOptions.placeholderOf(R.drawable.ic_loading)
                            .error(R.drawable.ic_error)
                    )
                    .into(imgItemBg)
            }
            loadingState(false)
        }
    }

    private fun setTvShow() {
        lifecycleScope.launch {
            tvShowViewModel.getDetailsTvShow(id).collectLatest { movie ->
                when (movie) {
                    is Resource.Loading -> loadingState(true)
                    is Resource.Success -> {
                        loadingState(false)
                        loadDataTvShow(movie.data)
                        movie.data?.let { tvShowViewModel.setTvShow(it) }
                        setFavoriteState(movie.data?.isFavorite)
                    }
                    is Resource.Error -> {
                        loadingState(false)
                        Toast.makeText(
                            this@DetailActivity,
                            "Gagal memuat data\n${movie.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    else -> Toast.makeText(
                        this@DetailActivity,
                        "Sesuatu Bermasalah",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun loadDataTvShow(item: TvShowDetail?) {
        if (item != null) {
            val scoreGenreReleaseDate = resources.getString(
                R.string.score_genre_release_date,
                item.score.toString(),
                GenreGenerator.create(item.genres),
                item.releaseDate
            )
            with(binding) {
                collapsing.title = item.title
                tvDescDetail.text = item.description
                tvScoreGenreReleaseDate.text = scoreGenreReleaseDate
                Glide.with(this@DetailActivity)
                    .load(ImageUrl.posterBig(item.imgPoster))
                    .apply(
                        RequestOptions.placeholderOf(R.drawable.ic_loading)
                            .error(R.drawable.ic_error)
                    )
                    .into(imgItemPhoto)
                Glide.with(this@DetailActivity)
                    .load(ImageUrl.background(item.imgBackground))
                    .apply(
                        RequestOptions.placeholderOf(R.drawable.ic_loading)
                            .error(R.drawable.ic_error)
                    )
                    .into(imgItemBg)
            }
            loadingState(false)
        }
    }

    private fun onClickFavorite() {
        if (category == "movie") {
            movieViewModel.setFavoriteMovie()
        } else {
            tvShowViewModel.setFavoriteTvShow()
        }
    }

    private fun setFavoriteState(state: Boolean?) {
        val fab = binding.fabAddToFavorite
        if (state == true) {
            fab.setImageResource(R.drawable.ic_favorite_filled)
        } else {
            fab.setImageResource(R.drawable.ic_favorite_border)
        }
    }


    private fun loadingState(state: Boolean) {
        binding.progressBar.isVisible = state
        binding.nestedScrollView.isInvisible = state
        binding.appBar.isInvisible = state
        binding.fabAddToFavorite.isInvisible = state
    }

}