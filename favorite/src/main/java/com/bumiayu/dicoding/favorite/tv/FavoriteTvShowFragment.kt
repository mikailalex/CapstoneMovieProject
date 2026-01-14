package com.bumiayu.dicoding.favorite.tv

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumiayu.dicoding.capstonemovieproject.core.domain.model.tvshow.TvShowDetail
import com.bumiayu.dicoding.capstonemovieproject.core.ui.BaseFragment
import com.bumiayu.dicoding.capstonemovieproject.ui.detail.DetailActivity
import com.bumiayu.dicoding.capstonemovieproject.ui.tv.TvShowViewModel
import com.bumiayu.dicoding.favorite.databinding.FragmentFavoriteTvShowBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class FavoriteTvShowFragment :
    BaseFragment<FragmentFavoriteTvShowBinding>({ FragmentFavoriteTvShowBinding.inflate(it) }),
    FavoriteTvShowAdapter.IOnClickListener {

    private val viewModel: TvShowViewModel by activityViewModel()
    private val adapterTvShow = FavoriteTvShowAdapter(this)

    override fun FragmentFavoriteTvShowBinding.onViewCreated(savedInstanceState: Bundle?) {

        adapterConfig()

        loadStateConfig(adapterTvShow)
    }

    private fun adapterConfig() {
        val orientation = resources.configuration.orientation
        binding?.rvFavoriteTvShow?.apply {
            layoutManager =
                if (orientation == Configuration.ORIENTATION_PORTRAIT) LinearLayoutManager(
                    context
                ) else GridLayoutManager(context, 2)
            setHasFixedSize(true)
            adapter = adapterTvShow
        }

        lifecycleScope.launch {
            viewModel.getFavoriteTvShows.collectLatest { pagingData ->
                adapterTvShow.submitData(pagingData)
            }
        }
    }

    private fun loadStateConfig(adapterTvShow: FavoriteTvShowAdapter) {
        adapterTvShow.addLoadStateListener { loadState ->
            binding?.tvFavoriteTvShowEmpty?.isVisible =
                loadState.source.refresh is LoadState.NotLoading && adapterTvShow.itemCount == 0
            binding?.rvFavoriteTvShow?.isInvisible =
                loadState.source.refresh is LoadState.NotLoading && adapterTvShow.itemCount == 0
        }
    }

    override fun onClick(tvShow: TvShowDetail) {
        val intent = Intent(requireActivity(), DetailActivity::class.java)
        intent.putExtra(DetailActivity.EXTRA_ID, tvShow.id)
        intent.putExtra(DetailActivity.EXTRA_CATEGORY, "tvShow")
        startActivity(intent)
    }
}