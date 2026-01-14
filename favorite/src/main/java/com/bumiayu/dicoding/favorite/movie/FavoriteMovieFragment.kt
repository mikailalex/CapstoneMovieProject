package com.bumiayu.dicoding.favorite.movie

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumiayu.dicoding.capstonemovieproject.core.domain.model.movie.MovieDetail
import com.bumiayu.dicoding.capstonemovieproject.core.ui.BaseFragment
import com.bumiayu.dicoding.capstonemovieproject.ui.detail.DetailActivity
import com.bumiayu.dicoding.capstonemovieproject.ui.movie.MovieViewModel
import com.bumiayu.dicoding.favorite.databinding.FragmentFavoriteMovieBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class FavoriteMovieFragment :
    BaseFragment<FragmentFavoriteMovieBinding>({ FragmentFavoriteMovieBinding.inflate(it) }),
    FavoriteMovieAdapter.IOnClickListener {

    private val viewModel: MovieViewModel by activityViewModel()
    private val adapterMovie = FavoriteMovieAdapter(this)

    override fun FragmentFavoriteMovieBinding.onViewCreated(savedInstanceState: Bundle?) {

        adapterConfig()

        loadStateConfig(adapterMovie)
    }

    private fun adapterConfig() {
        val orientation = resources.configuration.orientation
        binding?.rvFavoriteMovie?.apply {
            layoutManager =
                if (orientation == Configuration.ORIENTATION_PORTRAIT) LinearLayoutManager(
                    context
                ) else GridLayoutManager(context, 2)
            setHasFixedSize(true)
            adapter = adapterMovie
        }

        lifecycleScope.launch {
            viewModel.getFavoriteMovies.collectLatest { pagingData ->
                adapterMovie.submitData(pagingData)
            }
        }
    }

    private fun loadStateConfig(adapterMovie: FavoriteMovieAdapter) {
        adapterMovie.addLoadStateListener { loadState ->
            binding?.tvFavoriteMovieEmpty?.isVisible =
                loadState.source.refresh is LoadState.NotLoading && adapterMovie.itemCount == 0
            binding?.rvFavoriteMovie?.isInvisible =
                loadState.source.refresh is LoadState.NotLoading && adapterMovie.itemCount == 0
        }
    }

    override fun onClick(movie: MovieDetail) {
        val intent = Intent(requireActivity(), DetailActivity::class.java)
        intent.putExtra(DetailActivity.EXTRA_ID, movie.id)
        intent.putExtra(DetailActivity.EXTRA_CATEGORY, "movie")
        startActivity(intent)
    }
}