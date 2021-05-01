package com.bumiayu.dicoding.capstonemovieproject.core.data.source

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.bumiayu.dicoding.capstonemovieproject.core.data.source.local.entities.MovieEntity
import com.bumiayu.dicoding.capstonemovieproject.core.data.source.local.entities.RemoteKeys
import com.bumiayu.dicoding.capstonemovieproject.core.data.source.local.room.AppDatabase
import com.bumiayu.dicoding.capstonemovieproject.core.data.source.remote.network.ApiService
import com.bumiayu.dicoding.capstonemovieproject.core.utils.DataMapper
import retrofit2.HttpException
import java.io.IOException

private const val STARTING_PAGE_INDEX = 1

@ExperimentalPagingApi
class RemoteMediatorMovie(
    private val type: TypePagingData,
    private val service: ApiService,
    private val database: AppDatabase,
    private val query: String = ""
) : RemoteMediator<Int, MovieEntity>() {

    override suspend fun initialize(): InitializeAction {
        // Require that remote REFRESH is launched on initial load and succeeds before launching
        // remote PREPEND / APPEND.
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }


    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MovieEntity>
    ): MediatorResult {

        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = database.remoteKeysDao().remoteKeysRepoId()
                remoteKeys?.nextKey?.minus(1) ?: STARTING_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKeys = database.remoteKeysDao().remoteKeysRepoId()
                // If remoteKeys is null, that means the refresh result is not in the database yet.
                // We can return Success with `endOfPaginationReached = false` because Paging
                // will call this method again if RemoteKeys becomes non-null.
                // If remoteKeys is NOT NULL but its prevKey is null, that means we've reached
                // the end of pagination for prepend.
                val prevKey = remoteKeys?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = database.remoteKeysDao().remoteKeysRepoId()
                // If remoteKeys is null, that means the refresh result is not in the database yet.
                // We can return Success with `endOfPaginationReached = false` because Paging
                // will call this method again if RemoteKeys becomes non-null.
                // If remoteKeys is NOT NULL but its prevKey is null, that means we've reached
                // the end of pagination for append.
                val nextKey = remoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }
        }

        try {
            when (type) {
                TypePagingData.MOVIE_POPULAR -> {
                    val apiResponse = service.getPopularMovies(page = page)

                    val resultMovieEntity =
                        DataMapper.movieItemSearchToMovieEntityList(apiResponse.results)
                    val endOfPaginationReached = apiResponse.results.isEmpty()
                    database.withTransaction {
                        // clear all tables in the database
                        if (loadType == LoadType.REFRESH) {
                            database.remoteKeysDao().clearRemoteKeys()
                        }
                        val prevKey =
                            if (apiResponse.page == STARTING_PAGE_INDEX) null else apiResponse.page - 1
                        val nextKey = if (endOfPaginationReached) null else apiResponse.page + 1
                        val keys = RemoteKeys(
                            currentKey = apiResponse.page,
                            prevKey = prevKey,
                            nextKey = nextKey
                        )
                        database.remoteKeysDao().insert(keys)
                        database.movieDao().insertMovies(resultMovieEntity)
                    }
                    return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
                }
                TypePagingData.TV_SHOW_POPULAR -> {
                    service.getPopularTvShows(page = page)
                    return MediatorResult.Success(endOfPaginationReached = false)
                }
            }
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }
}