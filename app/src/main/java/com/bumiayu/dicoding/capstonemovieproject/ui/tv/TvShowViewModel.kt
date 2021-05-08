package com.bumiayu.dicoding.capstonemovieproject.ui.tv

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.bumiayu.dicoding.capstonemovieproject.core.domain.model.Resource
import com.bumiayu.dicoding.capstonemovieproject.core.domain.model.tvshow.TvShow
import com.bumiayu.dicoding.capstonemovieproject.core.domain.model.tvshow.TvShowDetail
import com.bumiayu.dicoding.capstonemovieproject.core.domain.usecase.TvShowUseCase
import kotlinx.coroutines.flow.Flow

class TvShowViewModel(private val useCase: TvShowUseCase): ViewModel() {

    private lateinit var tvShow: TvShowDetail

    fun setTvShow(tvShow: TvShowDetail) {
        this.tvShow = tvShow
    }

    fun getTvShows(sortBy: String): Flow<PagingData<TvShow>> =
        useCase.getTvShows(sortBy).cachedIn(viewModelScope)

    fun getPopularTvShows(): Flow<PagingData<TvShow>> =
        useCase.getPopularTvShows().cachedIn(viewModelScope)

    fun getTvShowOnTheAir(): Flow<PagingData<TvShow>> =
        useCase.getTvShowOnTheAir().cachedIn(viewModelScope)

    fun getSearchTvShows(query: String): Flow<PagingData<TvShow>> =
        useCase.getSearchTvShows(query).cachedIn(viewModelScope)

    fun getDetailsTvShow(TvShowId: Int): Flow<Resource<TvShowDetail>> =
        useCase.getDetailsTvShow(TvShowId)

    fun getFavoriteTvShows(): Flow<PagingData<TvShowDetail>> =
        useCase.getFavoriteTvShows().cachedIn(viewModelScope)

    fun setFavoriteTvShow() =
        useCase.setFavoriteTvShow(tvShow)
}