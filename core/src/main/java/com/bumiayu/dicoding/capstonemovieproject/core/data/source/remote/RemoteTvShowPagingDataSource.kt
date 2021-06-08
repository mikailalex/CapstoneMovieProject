package com.bumiayu.dicoding.capstonemovieproject.core.data.source.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.bumiayu.dicoding.capstonemovieproject.core.data.source.TypeRequestDataTvShow
import com.bumiayu.dicoding.capstonemovieproject.core.data.source.local.AppDatabase
import com.bumiayu.dicoding.capstonemovieproject.core.data.source.local.entities.RemoteKeys
import com.bumiayu.dicoding.capstonemovieproject.core.data.source.local.entities.TvShowEntity
import com.bumiayu.dicoding.capstonemovieproject.core.data.source.remote.network.ApiService
import com.bumiayu.dicoding.capstonemovieproject.core.utils.ext.toListTvShowEntity
import com.bumptech.glide.load.HttpException
import java.io.IOException

class RemoteTvShowPagingDataSource(
    private val typeRequest: TypeRequestDataTvShow,
    private val apiService: ApiService,
    private val appDataBase: AppDatabase,
    private val query: String? = ""
) : PagingSource<Int, TvShowEntity>() {
    private val startingPageIndex = 1
    private var idRemoteKey = 0

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TvShowEntity> {
        // Initial load params.key == null
        val page = params.key ?: startingPageIndex
        return try {
            when (typeRequest) {
                TypeRequestDataTvShow.TVSHOW_SEARCH -> {
                    idRemoteKey = 5
                    val response = apiService.getSearchTvShows(query = query, page = page)
                    val tvShowEntity = response.results.toListTvShowEntity()

                    val nextKey = if (tvShowEntity.isEmpty()) {
                        null
                    } else {
                        appDataBase.tvShowDao().insertTvShows(tvShowEntity)
                        page + 1
                    }
                    appDataBase.remoteKeysDao().insert(
                        RemoteKeys(page, page - 1, nextKey, idRemoteKey)
                    )
                    LoadResult.Page(
                        data = tvShowEntity,
                        prevKey = if (page == startingPageIndex) null else page - 1,
                        nextKey = nextKey
                    )

                }
                TypeRequestDataTvShow.TVSHOW_POPULAR -> {
                    idRemoteKey = 6
                    val response = apiService.getPopularTvShows(page = page)
                    val tvShowEntity = response.results.toListTvShowEntity()

                    val nextKey = if (tvShowEntity.isEmpty()) {
                        null
                    } else {
                        appDataBase.tvShowDao().insertTvShows(tvShowEntity)
                        page + 1
                    }
                    appDataBase.remoteKeysDao().insert(
                        RemoteKeys(page, page - 1, nextKey, idRemoteKey)
                    )
                    LoadResult.Page(
                        data = tvShowEntity,
                        prevKey = if (page == startingPageIndex) null else page - 1,
                        nextKey = nextKey
                    )
                }
                TypeRequestDataTvShow.TVSHOW_ON_THE_AIR -> {
                    idRemoteKey = 7
                    val response = apiService.getOnTheAirTvShows(page = page)
                    val tvShowEntity = response.results.toListTvShowEntity()

                    val nextKey = if (tvShowEntity.isEmpty()) {
                        null
                    } else {
                        appDataBase.tvShowDao().insertTvShows(tvShowEntity)
                        page + 1
                    }
                    appDataBase.remoteKeysDao().insert(
                        RemoteKeys(page, page - 1, nextKey, idRemoteKey)
                    )
                    LoadResult.Page(
                        data = tvShowEntity,
                        prevKey = if (page == startingPageIndex) null else page - 1,
                        nextKey = nextKey
                    )
                }
                TypeRequestDataTvShow.TVSHOW_TOP_RATED -> {
                    idRemoteKey = 8
                    val response = apiService.getTopRatedTvShows(page = page)
                    val tvShowEntity = response.results.toListTvShowEntity()

                    val nextKey = if (tvShowEntity.isEmpty()) {
                        null
                    } else {
                        appDataBase.tvShowDao().insertTvShows(tvShowEntity)
                        page + 1
                    }
                    appDataBase.remoteKeysDao().insert(
                        RemoteKeys(page, page - 1, nextKey, idRemoteKey)
                    )
                    LoadResult.Page(
                        data = tvShowEntity,
                        prevKey = if (page == startingPageIndex) null else page - 1,
                        nextKey = nextKey
                    )
                }
            }
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

    // The refresh key is used for subsequent refresh calls to PagingSource.load after the initial load
    override fun getRefreshKey(state: PagingState<Int, TvShowEntity>): Int? {
        // We use the remotekeys in database to get the number of page
        // the return will be sent to load() as params.key
        return appDataBase.remoteKeysDao().getRemoteKey(idRemoteKey)?.nextKey
    }
}