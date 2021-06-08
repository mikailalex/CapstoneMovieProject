package com.bumiayu.dicoding.capstonemovieproject.core.domain.usecase

import androidx.paging.PagingData
import com.bumiayu.dicoding.capstonemovieproject.core.domain.model.Resource
import com.bumiayu.dicoding.capstonemovieproject.core.domain.model.tvshow.TvShow
import com.bumiayu.dicoding.capstonemovieproject.core.domain.model.tvshow.TvShowDetail
import com.bumiayu.dicoding.capstonemovieproject.core.domain.repository.ITvShowRepository
import kotlinx.coroutines.flow.Flow

class TvShowInteractor(private val tvShowRepository: ITvShowRepository): TvShowUseCase {
    override fun getTvShows(sortBy: String): Flow<PagingData<TvShow>> = tvShowRepository.getTvShows(sortBy)

    override fun getPopularTvShows(): Flow<PagingData<TvShow>> = tvShowRepository.getPopularTvShows()

    override fun getOnTheAirTvShows(): Flow<PagingData<TvShow>> = tvShowRepository.getOnTheAirTvShows()

    override fun getTopRatedTvShows(): Flow<PagingData<TvShow>> = tvShowRepository.getTopRatedTvShows()

    override fun getSearchTvShows(query: String?): Flow<PagingData<TvShow>> = tvShowRepository.getSearchTvShows(query)

    override fun getDetailsTvShow(tvShowId: Int): Flow<Resource<TvShowDetail>> = tvShowRepository.getDetailsTvShow(tvShowId)

    override fun getFavoriteTvShows(): Flow<PagingData<TvShowDetail>> = tvShowRepository.getFavoriteTvShows()

    override fun setFavoriteTvShow(tvShow: TvShowDetail) = tvShowRepository.setFavoriteTvShow(tvShow)

}