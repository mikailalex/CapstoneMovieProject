package com.bumiayu.dicoding.capstonemovieproject.ui.tv

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumiayu.dicoding.capstonemovieproject.R
import com.bumiayu.dicoding.capstonemovieproject.core.domain.model.tvshow.TvShow
import com.bumiayu.dicoding.capstonemovieproject.core.ui.BaseFragment
import com.bumiayu.dicoding.capstonemovieproject.databinding.FragmentTvShowBinding
import com.bumiayu.dicoding.capstonemovieproject.ui.detail.DetailActivity
import com.bumiayu.dicoding.capstonemovieproject.ui.detail.DetailActivity.Companion.EXTRA_CATEGORY
import com.bumiayu.dicoding.capstonemovieproject.ui.detail.DetailActivity.Companion.EXTRA_ID
import com.bumiayu.dicoding.capstonemovieproject.ui.movie.MovieAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class TvShowFragment : BaseFragment<FragmentTvShowBinding>({ FragmentTvShowBinding.inflate(it) }), TvShowAdapter.IOnclickListener {

    private val viewModel: TvShowViewModel by viewModel()
    private val adapterTvPopular = TvShowAdapter(this)
    private val adapterTvOnTheAir = TvShowAdapter(this)
    private val adapterOtherTv = TvShowAdapter(this)

    override fun FragmentTvShowBinding.onViewCreated(savedInstanceState: Bundle?) {
        initAdapter()
        lifecycleScope.launch {
            viewModel.getTvShowOnTheAir().onEach { adapterTvOnTheAir.submitData(it) }.launchIn(this)
            viewModel.getPopularTvShows().onEach { adapterTvPopular.submitData(it) }.launchIn(this)
            viewModel.getTvShows("Title").collectLatest { adapterOtherTv.submitData(it) }
        }
    }

    private fun initAdapter() {
        val orientation = resources.configuration.orientation
        binding?.rvTvOnTheAir?.apply {
            layoutManager =
                if (orientation == Configuration.ORIENTATION_PORTRAIT) GridLayoutManager(
                    context,2, GridLayoutManager.HORIZONTAL, false
                ) else GridLayoutManager(context, 1, GridLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            adapter = adapterTvOnTheAir
        }

        binding?.rvTvPopular?.apply {
            layoutManager =
                if (orientation == Configuration.ORIENTATION_PORTRAIT) GridLayoutManager(
                    context,2, GridLayoutManager.HORIZONTAL, false
                ) else GridLayoutManager(context, 1, GridLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            adapter = adapterTvPopular
        }

        binding?.rvTvOther?.apply {
            layoutManager =
                if (orientation == Configuration.ORIENTATION_PORTRAIT) GridLayoutManager(
                    context,3
                ) else GridLayoutManager(context, 6)
            setHasFixedSize(true)
            adapter = adapterOtherTv
        }

        loadStateConfig(adapterTvOnTheAir)
        loadStateConfig(adapterTvPopular)
        loadStateConfig(adapterOtherTv)
    }

    private fun loadStateConfig(adapter: TvShowAdapter) {
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

    override fun onClick(tvShow: TvShow) {
        val intent = Intent(requireActivity(), DetailActivity::class.java)
        intent.putExtra(EXTRA_ID, tvShow.id)
        intent.putExtra(EXTRA_CATEGORY, "tvShow")
        startActivity(intent)
    }
}