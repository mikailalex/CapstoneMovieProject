package com.bumiayu.dicoding.capstonemovieproject.ui.movie

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import com.bumiayu.dicoding.capstonemovieproject.core.domain.model.movie.Movie
import com.bumiayu.dicoding.capstonemovieproject.core.ui.BaseFragment
import com.bumiayu.dicoding.capstonemovieproject.databinding.FragmentMovieBinding
import com.bumiayu.dicoding.capstonemovieproject.ui.detail.DetailActivity
import com.bumiayu.dicoding.capstonemovieproject.ui.detail.DetailActivity.Companion.EXTRA_CATEGORY
import com.bumiayu.dicoding.capstonemovieproject.ui.detail.DetailActivity.Companion.EXTRA_ID
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class MovieFragment : BaseFragment<FragmentMovieBinding>({ FragmentMovieBinding.inflate(it) }),
    MovieAdapter.IOnClickListener {

    private val viewModel: MovieViewModel by viewModels<>()
    private val adapterMoviePopular = MovieAdapter(this)
    private val adapterMovieInTheater = MovieAdapter(this)
    private val adapterOtherMovie = MovieAdapter(this)

    override fun FragmentMovieBinding.onViewCreated(savedInstanceState: Bundle?) {
        initAdapter()
        lifecycleScope.launch {
            viewModel.getNowPlayingMovies.onEach { adapterMovieInTheater.submitData(it) }.launchIn(this)
            viewModel.getPopularMovies.onEach { adapterMoviePopular.submitData(it) }.launchIn(this)
            viewModel.getMovies.collectLatest { adapterOtherMovie.submitData(it) }
        }
        binding?.retryButton?.setOnClickListener { adapterMoviePopular.retry() }
    }

    private fun initAdapter() {
        val orientation = resources.configuration.orientation
        val decoration = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        binding?.rvMoviesInTheater?.apply {
            layoutManager =
                if (orientation == Configuration.ORIENTATION_PORTRAIT) GridLayoutManager(
                    context,2, GridLayoutManager.HORIZONTAL, false
                ) else GridLayoutManager(context, 1, GridLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            addItemDecoration(decoration)
            adapter = adapterMovieInTheater
        }

        binding?.rvMoviesPopular?.apply {
            layoutManager =
                if (orientation == Configuration.ORIENTATION_PORTRAIT) GridLayoutManager(
                    context,2, GridLayoutManager.HORIZONTAL, false
                ) else GridLayoutManager(context, 1, GridLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            addItemDecoration(decoration)
            adapter = adapterMoviePopular
        }

        binding?.rvOtherMovies?.apply {
            layoutManager =
                if (orientation == Configuration.ORIENTATION_PORTRAIT) GridLayoutManager(
                    context,3
                ) else GridLayoutManager(context, 6)
            setHasFixedSize(true)
            addItemDecoration(decoration)
            adapter = adapterOtherMovie
        }

        loadStateConfig(adapterMovieInTheater)
        loadStateConfig(adapterMoviePopular)
        loadStateConfig(adapterOtherMovie)
    }

    private fun loadStateConfig(adapter: MovieAdapter) {
        adapter.addLoadStateListener { loadState ->
            // Show loading spinner during initial load or refresh.
            binding?.progressBar?.isVisible = loadState.source.refresh is LoadState.Loading
            // Only show the list if refresh succeeds.
            binding?.rvContainer?.isVisible = loadState.source.refresh is LoadState.NotLoading
            // Show the retry state if initial load or refresh fails.
            binding?.retryButton?.isVisible = loadState.source.refresh is LoadState.Error
            // Toast on any error, regardless of whether it came from RemoteMediator or PagingSource
            val errorState = loadState.source.append as? LoadState.Error
                ?: loadState.source.prepend as? LoadState.Error
                ?: loadState.append as? LoadState.Error
                ?: loadState.prepend as? LoadState.Error
            errorState?.let {
                Toast.makeText(
                    requireContext(),
                    "\uD83D\uDE28 Wooops ${it.error}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun onClick(movie: Movie) {
        val intent = Intent(requireActivity(), DetailActivity::class.java)
        intent.putExtra(EXTRA_ID, movie.id)
        intent.putExtra(EXTRA_CATEGORY, "movie")
        startActivity(intent)
    }
}