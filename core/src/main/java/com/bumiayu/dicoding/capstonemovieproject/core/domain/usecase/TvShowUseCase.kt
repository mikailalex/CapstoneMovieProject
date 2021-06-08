package com.bumiayu.dicoding.capstonemovieproject.core.domain.usecase

import androidx.paging.PagingData
import com.bumiayu.dicoding.capstonemovieproject.core.domain.model.Resource
import com.bumiayu.dicoding.capstonemovieproject.core.domain.model.tvshow.TvShow
import com.bumiayu.dicoding.capstonemovieproject.core.domain.model.tvshow.TvShowDetail
import kotlinx.coroutines.flow.Flow

interface TvShowUseCase {
    fun getTvShows(sortBy: String): Flow<PagingData<TvShow>>
    fun getPopularTvShows(): Flow<PagingData<TvShow>>
    fun getOnTheAirTvShows(): Flow<PagingData<TvShow>>
    fun getTopRatedTvShows(): Flow<PagingData<TvShow>>
    fun getSearchTvShows(query: String?): Flow<PagingData<TvShow>>
    fun getDetailsTvShow(tvShowId: Int): Flow<Resource<TvShowDetail>>
    fun getFavoriteTvShows(): Flow<PagingData<TvShowDetail>>
    fun setFavoriteTvShow(tvShow: TvShowDetail)
}