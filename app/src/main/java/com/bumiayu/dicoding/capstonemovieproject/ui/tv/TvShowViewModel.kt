package com.bumiayu.dicoding.capstonemovieproject.ui.tv

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.bumiayu.dicoding.capstonemovieproject.core.domain.model.Resource
import com.bumiayu.dicoding.capstonemovieproject.core.domain.model.tvshow.TvShow
import com.bumiayu.dicoding.capstonemovieproject.core.domain.model.tvshow.TvShowDetail
import com.bumiayu.dicoding.capstonemovieproject.core.domain.usecase.TvShowUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*

class TvShowViewModel(private val useCase: TvShowUseCase) : ViewModel() {

    private lateinit var tvShow: TvShowDetail

    private val searchQuery = MutableStateFlow("")

    fun setTvShow(tvShow: TvShowDetail) {
        this.tvShow = tvShow
    }

    fun setSearchQuery(query: String) {
        searchQuery.value = query
    }

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val searchTvShows: Flow<PagingData<TvShow>> = searchQuery
        .debounce(400)
        .filter { it.isNotEmpty() }
        .distinctUntilChanged()
        .flatMapLatest { query ->
            useCase.getSearchTvShows(query)
        }
        .cachedIn(viewModelScope)

    val getTvShows: Flow<PagingData<TvShow>> = useCase.getTvShows("Title").cachedIn(viewModelScope)

    val getPopularTvShows: Flow<PagingData<TvShow>> =
        useCase.getPopularTvShows().cachedIn(viewModelScope)

    val getOnTheAirTvShows: Flow<PagingData<TvShow>> =
        useCase.getOnTheAirTvShows().cachedIn(viewModelScope)

    val getTopRatedTvShows: Flow<PagingData<TvShow>> =
        useCase.getTopRatedTvShows().cachedIn(viewModelScope)

    fun getDetailsTvShow(TvShowId: Int): Flow<Resource<TvShowDetail>> =
        useCase.getDetailsTvShow(TvShowId)

    val getFavoriteTvShows: Flow<PagingData<TvShowDetail>> =
        useCase.getFavoriteTvShows().cachedIn(viewModelScope)

    fun setFavoriteTvShow() =
        useCase.setFavoriteTvShow(tvShow)
}