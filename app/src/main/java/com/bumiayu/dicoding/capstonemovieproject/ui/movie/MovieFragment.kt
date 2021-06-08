package com.bumiayu.dicoding.capstonemovieproject.ui.movie

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.bumiayu.dicoding.capstonemovieproject.core.domain.model.movie.Movie
import com.bumiayu.dicoding.capstonemovieproject.core.ui.BaseFragment
import com.bumiayu.dicoding.capstonemovieproject.databinding.FragmentMovieBinding
import com.bumiayu.dicoding.capstonemovieproject.ui.detail.DetailActivity
import com.bumiayu.dicoding.capstonemovieproject.ui.detail.DetailActivity.Companion.EXTRA_CATEGORY
import com.bumiayu.dicoding.capstonemovieproject.ui.detail.DetailActivity.Companion.EXTRA_ID
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import ru.ldralighieri.corbind.widget.textChanges

class MovieFragment :
    BaseFragment<FragmentMovieBinding>({ FragmentMovieBinding.inflate(it) }),
    MovieAdapter.IOnClickListener {

    private val viewModel: MovieViewModel by sharedViewModel()
    private val adapterMovie = MovieAdapter(this)
    private lateinit var jobViewModel: Job

    companion object {
        const val ARG_TAB_POSITION = "tab_position"
    }

    // helper, is User ever use search feature, to avoid initial adapter
    private var isSearchUsed = false

    @FlowPreview
    override fun FragmentMovieBinding.onViewCreated(savedInstanceState: Bundle?) {
        initAdapter()
        val tabPosition = arguments?.getInt(ARG_TAB_POSITION)
        jobViewModel = lifecycleScope.launchWhenCreated {
            when (tabPosition) {
                1 -> {
                    binding?.apply {
                        searchBox.cardSearchBox.visibility = View.VISIBLE
                        rvMovies.setPadding(0, 120, 0, 0)
                        pleaseSearch.isVisible = true
                        searchBox.etSearchBox.textChanges().distinctUntilChanged()
                            .debounce(400)
                            .filter { it.isNotEmpty() }
                            .onEach {
                                isSearchUsed = true
                                viewModel.getSearchMovies(it.toString())
                                    .observe(viewLifecycleOwner) { pagingData ->
                                        pleaseSearch.isVisible = false
                                        adapterMovie.submitData(lifecycle, pagingData)
                                    }
                            }.launchIn(this@launchWhenCreated)
                    }
                }
                2 -> viewModel.getPopularMovies.collectLatest { adapterMovie.submitData(it) }
                3 -> viewModel.getNowPlayingMovies.collectLatest { adapterMovie.submitData(it) }
                4 -> viewModel.getTopRatedMovies.collectLatest { adapterMovie.submitData(it) }
                5 -> viewModel.getMovies.collectLatest { adapterMovie.submitData(it) }
            }
        }

        binding?.retryButton?.setOnClickListener { adapterMovie.retry() }
    }

    private fun initAdapter() {
        val orientation = resources.configuration.orientation

        binding?.rvMovies?.apply {
            layoutManager =
                if (orientation == Configuration.ORIENTATION_PORTRAIT) GridLayoutManager(
                    context, 4
                ) else GridLayoutManager(context, 8)
            setHasFixedSize(true)
            adapter = adapterMovie
        }

        loadStateConfig(adapterMovie)
    }

    private fun loadStateConfig(adapter: MovieAdapter) {
        adapter.addLoadStateListener { loadState ->
            binding?.apply {
                // Show loading spinner during initial load or refresh.
                progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                // Only show the list if refresh succeeds.
                rvMovies.isVisible = loadState.source.refresh is LoadState.NotLoading
                // Show the retry state if initial load or refresh fails.
                retryButton.isVisible = loadState.source.refresh is LoadState.Error
                // Show text if data empty
                if (isSearchUsed) emptyList.isVisible =
                    loadState.source.refresh is LoadState.NotLoading && adapter.itemCount == 0
            }
            // Toast on any error came from PagingSource
            val errorState = loadState.source.append as? LoadState.Error
                ?: loadState.source.prepend as? LoadState.Error
                ?: loadState.source.refresh as? LoadState.Error
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

    override fun onDestroyView() {
        jobViewModel.cancel()
        binding?.rvMovies?.adapter = null
        super.onDestroyView()
    }
}