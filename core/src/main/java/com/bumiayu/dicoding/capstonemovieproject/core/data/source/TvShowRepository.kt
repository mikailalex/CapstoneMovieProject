package com.bumiayu.dicoding.capstonemovieproject.core.data.source

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.bumiayu.dicoding.capstonemovieproject.core.data.source.local.AppDatabase
import com.bumiayu.dicoding.capstonemovieproject.core.data.source.remote.RemoteTvShowPagingDataSource
import com.bumiayu.dicoding.capstonemovieproject.core.data.source.remote.network.ApiResponse
import com.bumiayu.dicoding.capstonemovieproject.core.data.source.remote.network.ApiService
import com.bumiayu.dicoding.capstonemovieproject.core.data.source.remote.response.TvShowDetailResponse
import com.bumiayu.dicoding.capstonemovieproject.core.domain.model.Resource
import com.bumiayu.dicoding.capstonemovieproject.core.domain.model.tvshow.TvShow
import com.bumiayu.dicoding.capstonemovieproject.core.domain.model.tvshow.TvShowDetail
import com.bumiayu.dicoding.capstonemovieproject.core.domain.repository.ITvShowRepository
import com.bumiayu.dicoding.capstonemovieproject.core.utils.SortUtils
import com.bumiayu.dicoding.capstonemovieproject.core.utils.SortUtils.TV_SHOW_TABLE
import com.bumiayu.dicoding.capstonemovieproject.core.utils.ext.toTvShow
import com.bumiayu.dicoding.capstonemovieproject.core.utils.ext.toTvShowDetail
import com.bumiayu.dicoding.capstonemovieproject.core.utils.ext.toTvShowDetailEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.io.IOException

class TvShowRepository(
    private val appDatabase: AppDatabase,
    private val apiService: ApiService
) : ITvShowRepository {

    // Offline
    override fun getTvShows(sortBy: String): Flow<PagingData<TvShow>> =
        Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false, maxSize = 120),
            pagingSourceFactory = {
                appDatabase.tvShowDao().getTvShows(SortUtils.getSortedQuery(sortBy, TV_SHOW_TABLE))
            }
        ).flow.map { pagingData ->
            pagingData.map { it.toTvShow() }
        }

    // Online
    override fun getPopularTvShows(): Flow<PagingData<TvShow>> =
        Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false, maxSize = 120),
            pagingSourceFactory = {
                RemoteTvShowPagingDataSource(
                    TypeRequestDataTvShow.TVSHOW_POPULAR,
                    apiService,
                    appDatabase
                )
            }
        ).flow.map { pagingData ->
            pagingData.map { it.toTvShow() }
        }

    // Online
    override fun getOnTheAirTvShows(): Flow<PagingData<TvShow>> =
        Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false, maxSize = 120),
            pagingSourceFactory = {
                RemoteTvShowPagingDataSource(
                    TypeRequestDataTvShow.TVSHOW_ON_THE_AIR,
                    apiService,
                    appDatabase
                )
            }
        ).flow.map { pagingData ->
            pagingData.map { it.toTvShow() }
        }

    override fun getTopRatedTvShows(): Flow<PagingData<TvShow>> =
        Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false, maxSize = 120),
            pagingSourceFactory = {
                RemoteTvShowPagingDataSource(
                    TypeRequestDataTvShow.TVSHOW_TOP_RATED,
                    apiService,
                    appDatabase
                )
            }
        ).flow.map { pagingData ->
            pagingData.map { it.toTvShow() }
        }

    // Online
    override fun getSearchTvShows(query: String?): Flow<PagingData<TvShow>> =
        Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false, maxSize = 120),
            pagingSourceFactory = {
                RemoteTvShowPagingDataSource(
                    TypeRequestDataTvShow.TVSHOW_SEARCH,
                    apiService,
                    appDatabase,
                    query
                )
            }
        ).flow.map { pagingData ->
            pagingData.map { it.toTvShow() }
        }

    // Online
    override fun getDetailsTvShow(tvShowId: Int): Flow<Resource<TvShowDetail>> =
        object : NetworkBoundResource<TvShowDetail, TvShowDetailResponse>() {
            override fun loadFromDB(): Flow<TvShowDetail?> =
                appDatabase.tvShowDao().getDetailTvShowById(tvShowId).map {
                    it.toTvShowDetail()
                }

            override fun shouldFetch(data: TvShowDetail?): Boolean = data == null

            override suspend fun createCall(): Flow<ApiResponse<TvShowDetailResponse>> =
                flow {
                    try {
                        val response = apiService.getDetailTvShow(tvShowId)
                        emit(ApiResponse.Success(response))
                    } catch (e: IOException) {
                        e.printStackTrace()
                        emit(ApiResponse.Error(e.message.toString()))
                    }
                }

            override suspend fun saveCallResult(data: TvShowDetailResponse) =
                appDatabase.tvShowDao().insertDetailTvShow(data.toTvShowDetailEntity())

        }.asFlow()

    // Offline
    override fun getFavoriteTvShows(): Flow<PagingData<TvShowDetail>> =
        Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false, maxSize = 120),
            pagingSourceFactory = { appDatabase.tvShowDao().getListFavoriteTvShows() }
        ).flow.map { pagingData ->
            pagingData.map { it.toTvShowDetail()!! }
        }

    override fun setFavoriteTvShow(tvShow: TvShowDetail) {
        CoroutineScope(Dispatchers.IO).launch {
            tvShow.isFavorite = !tvShow.isFavorite
            appDatabase.tvShowDao().updateDetailTvShow(tvShow.toTvShowDetailEntity())
        }
    }
}