package com.bumiayu.dicoding.capstonemovieproject.ui.tv

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
import com.bumiayu.dicoding.capstonemovieproject.core.domain.model.tvshow.TvShow
import com.bumiayu.dicoding.capstonemovieproject.core.ui.BaseFragment
import com.bumiayu.dicoding.capstonemovieproject.databinding.FragmentTvShowBinding
import com.bumiayu.dicoding.capstonemovieproject.ui.detail.DetailActivity
import com.bumiayu.dicoding.capstonemovieproject.ui.detail.DetailActivity.Companion.EXTRA_CATEGORY
import com.bumiayu.dicoding.capstonemovieproject.ui.detail.DetailActivity.Companion.EXTRA_ID
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import ru.ldralighieri.corbind.widget.textChanges

class TvShowFragment : BaseFragment<FragmentTvShowBinding>({ FragmentTvShowBinding.inflate(it) }),
    TvShowAdapter.IOnclickListener {

    private val viewModel: TvShowViewModel by activityViewModel()
    private val adapterTv = TvShowAdapter(this)

    // helper, is User ever use search feature, to avoid initial adapter
    private var isSearchUsed = false

    companion object {
        const val ARG_TAB_POSITION = "tab_position"
    }

    @FlowPreview
    override fun FragmentTvShowBinding.onViewCreated(savedInstanceState: Bundle?) {
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
                            adapterTv.submitData(lifecycle, PagingData.empty())
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

                        viewModel.searchTvShows.collectLatest { pagingData ->
                            adapterTv.submitData(pagingData)
                        }
                    }
                    2 -> viewModel.getPopularTvShows.collectLatest { adapterTv.submitData(it) }
                    3 -> viewModel.getOnTheAirTvShows.collectLatest { adapterTv.submitData(it) }
                    4 -> viewModel.getTopRatedTvShows.collectLatest { adapterTv.submitData(it) }
                    5 -> viewModel.getTvShows.collectLatest { adapterTv.submitData(it) }
                }
            }
        }
    }

    private fun initAdapter() {
        val orientation = resources.configuration.orientation
        binding?.rvTvShows?.apply {
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
            binding?.apply {
                progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                rvTvShows.isVisible = loadState.source.refresh is LoadState.NotLoading
                
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

    override fun onClick(tvShow: TvShow) {
        val intent = Intent(requireActivity(), DetailActivity::class.java)
        intent.putExtra(EXTRA_ID, tvShow.id)
        intent.putExtra(EXTRA_CATEGORY, "tvShow")
        startActivity(intent)
    }

    override fun onDestroyView() {
        binding?.rvTvShows?.adapter = null
        super.onDestroyView()
    }
}