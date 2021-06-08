package com.bumiayu.dicoding.capstonemovieproject.ui.tv

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.bumiayu.dicoding.capstonemovieproject.core.domain.model.tvshow.TvShow
import com.bumiayu.dicoding.capstonemovieproject.core.ui.BaseFragment
import com.bumiayu.dicoding.capstonemovieproject.databinding.FragmentTvShowBinding
import com.bumiayu.dicoding.capstonemovieproject.ui.detail.DetailActivity
import com.bumiayu.dicoding.capstonemovieproject.ui.detail.DetailActivity.Companion.EXTRA_CATEGORY
import com.bumiayu.dicoding.capstonemovieproject.ui.detail.DetailActivity.Companion.EXTRA_ID
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import ru.ldralighieri.corbind.widget.textChanges
import java.util.*

class TvShowFragment : BaseFragment<FragmentTvShowBinding>({ FragmentTvShowBinding.inflate(it) }),
    TvShowAdapter.IOnclickListener {

    private val viewModel: TvShowViewModel by sharedViewModel()
    private val adapterTv = TvShowAdapter(this)
    private lateinit var jobViewModel: Job

    // helper, is User ever use search feature, to avoid initial adapter
    private var isSearchUsed = false

    companion object {
        const val ARG_TAB_POSITION = "tab_position"
    }

    @FlowPreview
    override fun FragmentTvShowBinding.onViewCreated(savedInstanceState: Bundle?) {
        initAdapter()
        val tabPosition = arguments?.getInt(ARG_TAB_POSITION)
        jobViewModel = lifecycleScope.launchWhenCreated {
            when (tabPosition) {
                1 -> {
                    binding?.apply {
                        searchBox.cardSearchBox.visibility = View.VISIBLE
                        rvTvShow.setPadding(0, 120, 0, 0)
                        pleaseSearch.isVisible = true
                        searchBox.etSearchBox.textChanges().distinctUntilChanged()
                            .debounce(400)
                            .filter { it.isNotEmpty() }
                            .onEach {
                                isSearchUsed = true
                                viewModel.getSearchTvShows(it.toString())
                                    .observe(viewLifecycleOwner) { pagingData ->
                                        pleaseSearch.isVisible = false
                                        adapterTv.submitData(lifecycle, pagingData)
                                    }
                            }.launchIn(this@launchWhenCreated)
                    }
                }
                2 -> viewModel.getPopularTvShows.collectLatest { adapterTv.submitData(it) }
                3 -> viewModel.getOnTheAirTvShows.collectLatest { adapterTv.submitData(it) }
                4 -> viewModel.getTopRatedTvShows.collectLatest { adapterTv.submitData(it) }
                5 -> viewModel.getTvShows.collectLatest { adapterTv.submitData(it) }
            }
        }
        // To avoid crash when swipe tab layout but the parent layout still in animation
        binding?.animatedViewgroup?.layoutTransition?.setAnimateParentHierarchy(false)

        binding?.retryButton?.setOnClickListener { adapterTv.retry() }
    }

    private fun initAdapter() {
        val orientation = resources.configuration.orientation
        binding?.rvTvShow?.apply {
            layoutManager =
                if (orientation == Configuration.ORIENTATION_PORTRAIT) GridLayoutManager(
                    context, 4
                ) else GridLayoutManager(context, 8)
            setHasFixedSize(true)
            adapter = adapterTv
        }

        loadStateConfig(adapterTv)
    }

    private fun loadStateConfig(adapter: TvShowAdapter) {
        adapter.addLoadStateListener { loadState ->
            // Show loading spinner during initial load or refresh.
            binding?.apply {
                progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                // Only show the list if refresh succeeds.
                rvTvShow.isVisible = loadState.source.refresh is LoadState.NotLoading
                // Show the retry state if initial load or refresh fails.
                retryButton.isVisible = loadState.source.refresh is LoadState.Error
                // Show text if data empty
                if (isSearchUsed) emptyList.isVisible =
                    loadState.source.refresh is LoadState.NotLoading && adapter.itemCount == 0
            }
            // Toast on any error, regardless of whether it came from RemoteMediator or PagingSource
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

    override fun onClick(tvShow: TvShow) {
        val intent = Intent(requireActivity(), DetailActivity::class.java)
        intent.putExtra(EXTRA_ID, tvShow.id)
        intent.putExtra(EXTRA_CATEGORY, "tvShow")
        startActivity(intent)
    }

    override fun onDestroyView() {
        jobViewModel.cancel()
        binding?.rvTvShow?.adapter = null
        super.onDestroyView()
    }
}