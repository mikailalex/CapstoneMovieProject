package com.bumiayu.dicoding.capstonemovieproject.ui.tv

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.bumiayu.dicoding.capstonemovieproject.core.domain.model.Resource
import com.bumiayu.dicoding.capstonemovieproject.core.domain.model.tvshow.TvShow
import com.bumiayu.dicoding.capstonemovieproject.core.domain.model.tvshow.TvShowDetail
import com.bumiayu.dicoding.capstonemovieproject.core.domain.usecase.TvShowUseCase
import kotlinx.coroutines.flow.Flow

class TvShowViewModel(private val useCase: TvShowUseCase) : ViewModel() {

    private lateinit var tvShow: TvShowDetail

    fun setTvShow(tvShow: TvShowDetail) {
        this.tvShow = tvShow
    }

    val getTvShows: Flow<PagingData<TvShow>> = useCase.getTvShows("Title").cachedIn(viewModelScope)

    val getPopularTvShows: Flow<PagingData<TvShow>> =
        useCase.getPopularTvShows().cachedIn(viewModelScope)

    val getOnTheAirTvShows: Flow<PagingData<TvShow>> =
        useCase.getOnTheAirTvShows().cachedIn(viewModelScope)

    val getTopRatedTvShows: Flow<PagingData<TvShow>> =
        useCase.getTopRatedTvShows().cachedIn(viewModelScope)

    fun getSearchTvShows(query: String): LiveData<PagingData<TvShow>> =
        useCase.getSearchTvShows(query).cachedIn(viewModelScope).asLiveData()

    fun getDetailsTvShow(TvShowId: Int): Flow<Resource<TvShowDetail>> =
        useCase.getDetailsTvShow(TvShowId)

    val getFavoriteTvShows: Flow<PagingData<TvShowDetail>> =
        useCase.getFavoriteTvShows().cachedIn(viewModelScope)

    fun setFavoriteTvShow() =
        useCase.setFavoriteTvShow(tvShow)
}