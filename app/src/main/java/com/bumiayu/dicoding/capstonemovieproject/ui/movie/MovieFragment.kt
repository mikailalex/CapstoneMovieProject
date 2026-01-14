package com.bumiayu.dicoding.capstonemovieproject.ui.movie

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import com.bumiayu.dicoding.capstonemovieproject.core.domain.model.movie.Movie
import com.bumiayu.dicoding.capstonemovieproject.core.ui.BaseFragment
import com.bumiayu.dicoding.capstonemovieproject.databinding.FragmentMovieBinding
import com.bumiayu.dicoding.capstonemovieproject.ui.detail.DetailActivity
import com.bumiayu.dicoding.capstonemovieproject.ui.detail.DetailActivity.Companion.EXTRA_CATEGORY
import com.bumiayu.dicoding.capstonemovieproject.ui.detail.DetailActivity.Companion.EXTRA_ID
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import ru.ldralighieri.corbind.widget.textChanges

class MovieFragment :
    BaseFragment<FragmentMovieBinding>({ FragmentMovieBinding.inflate(it) }),
    MovieAdapter.IOnClickListener {

    private val viewModel: MovieViewModel by activityViewModel()
    private val adapterMovie = MovieAdapter(this)

    companion object {
        const val ARG_TAB_POSITION = "tab_position"
    }

    private var isSearchUsed = false

    @FlowPreview
    override fun FragmentMovieBinding.onViewCreated(savedInstanceState: Bundle?) {
        initAdapter()
        val tabPosition = arguments?.getInt(ARG_TAB_POSITION)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                when (tabPosition) {
                    1 -> {
                        searchBox.cardSearchBox.visibility = View.VISIBLE
                        pleaseSearch.isVisible = !isSearchUsed

                        searchBox.btnClear.setOnClickListener {
                            searchBox.etSearchBox.text?.clear()
                            isSearchUsed = false
                            pleaseSearch.isVisible = true
                            viewModel.setSearchQuery("")
                            adapterMovie.submitData(lifecycle, PagingData.empty())
                        }

                        searchBox.etSearchBox.textChanges()
                            .onEach {
                                val query = it.toString()
                                searchBox.btnClear.isVisible = query.isNotEmpty()
                                if (query.isNotEmpty()) {
                                    isSearchUsed = true
                                    pleaseSearch.isVisible = false
                                    viewModel.setSearchQuery(query)
                                }
                            }
                            .launchIn(this@repeatOnLifecycle)

                        viewModel.searchMovies.collectLatest { pagingData ->
                            adapterMovie.submitData(pagingData)
                        }
                    }
                    2 -> viewModel.getPopularMovies.collectLatest { adapterMovie.submitData(it) }
                    3 -> viewModel.getNowPlayingMovies.collectLatest { adapterMovie.submitData(it) }
                    4 -> viewModel.getTopRatedMovies.collectLatest { adapterMovie.submitData(it) }
                    5 -> viewModel.getMovies.collectLatest { adapterMovie.submitData(it) }
                }
            }
        }
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
                progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                rvMovies.isVisible = loadState.source.refresh is LoadState.NotLoading
                
                if (isSearchUsed) {
                    emptyList.isVisible = loadState.source.refresh is LoadState.NotLoading && adapter.itemCount == 0
                }
            }
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
        binding?.rvMovies?.adapter = null
        super.onDestroyView()
    }
}