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
import com.bumiayu.dicoding.capstonemovieproject.core.utils.ext.toListMovieEntity
import retrofit2.HttpException
import java.io.IOException

private const val STARTING_PAGE_INDEX = 1

@OptIn(ExperimentalPagingApi::class)
class RemoteMediatorMovie(
    private val service: ApiService,
    private val database: AppDatabase,
    private val query: String?
) : RemoteMediator<Int, MovieEntity>() {

    // Callback fired during initialization of a [PagingData] stream, before initial load.
    override suspend fun initialize(): InitializeAction {
        database.remoteKeysDao().clearRemoteKeys()
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MovieEntity>
    ): MediatorResult {

        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = database.remoteKeysDao().getRemoteKeys()
                remoteKeys?.currentKey ?: STARTING_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKeys = database.remoteKeysDao().getRemoteKeys()
                val prevKey = remoteKeys?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = database.remoteKeysDao().getRemoteKeys()
                val nextKey = remoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }
        }

        try {
            val apiResponse = service.getSearchMovies(query = query, page = page)

            val resultMovieEntity =
                apiResponse.results.toListMovieEntity()
            val endOfPaginationReached = apiResponse.results.isEmpty()
            database.withTransaction {
                val prevKey =
                    if (apiResponse.page == STARTING_PAGE_INDEX) null else apiResponse.page - 1
                val nextKey = if (endOfPaginationReached) null else apiResponse.page + 1
//                val keys = RemoteKeys(
//                    currentKey = apiResponse.page,
//                    prevKey = prevKey,
//                    nextKey = nextKey
//                )
//                database.remoteKeysDao().insert(keys)
                database.movieDao().insertMovies(resultMovieEntity)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }
}