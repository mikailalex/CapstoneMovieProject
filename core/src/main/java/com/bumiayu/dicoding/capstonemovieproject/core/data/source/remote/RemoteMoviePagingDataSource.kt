package com.bumiayu.dicoding.capstonemovieproject.core.data.source.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.bumiayu.dicoding.capstonemovieproject.core.data.source.TypeRequestDataMovie
import com.bumiayu.dicoding.capstonemovieproject.core.data.source.local.AppDatabase
import com.bumiayu.dicoding.capstonemovieproject.core.data.source.local.entities.MovieEntity
import com.bumiayu.dicoding.capstonemovieproject.core.data.source.local.entities.RemoteKeys
import com.bumiayu.dicoding.capstonemovieproject.core.data.source.remote.network.ApiService
import com.bumiayu.dicoding.capstonemovieproject.core.utils.ext.toListMovieEntity
import com.bumptech.glide.load.HttpException
import java.io.IOException

class RemoteMoviePagingDataSource(
    private val typeRequest: TypeRequestDataMovie,
    private val apiService: ApiService,
    private val appDataBase: AppDatabase,
    private val query: String? = ""
) : PagingSource<Int, MovieEntity>() {

    private val startingPageIndex = 1
    private var idRemoteKey = 0

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieEntity> {
        // Initial load params.key == null
        val page = params.key ?: startingPageIndex
        return try {
            when (typeRequest) {
                TypeRequestDataMovie.MOVIE_SEARCH -> {
                    idRemoteKey = 1
                    val response = apiService.getSearchMovies(query = query, page = page)
                    val movieEntity = response.results.toListMovieEntity()

                    val nextKey = if (movieEntity.isEmpty()) {
                        null
                    } else {
                        appDataBase.movieDao().insertMovies(movieEntity)
                        page + 1
                    }
                    appDataBase.remoteKeysDao().insert(
                        RemoteKeys(page, page - 1, nextKey, idRemoteKey)
                    )
                    LoadResult.Page(
                        data = movieEntity,
                        prevKey = if (page == startingPageIndex) null else page - 1,
                        nextKey = nextKey
                    )
                }
                TypeRequestDataMovie.MOVIE_POPULAR -> {
                    idRemoteKey = 2
                    val response = apiService.getPopularMovies(page = page)
                    val movieEntity = response.results.toListMovieEntity()

                    val nextKey = if (movieEntity.isEmpty()) {
                        null
                    } else {
                        appDataBase.movieDao().insertMovies(movieEntity)
                        page + 1
                    }
                    appDataBase.remoteKeysDao().insert(
                        RemoteKeys(page, page - 1, nextKey, idRemoteKey)
                    )
                    LoadResult.Page(
                        data = movieEntity,
                        prevKey = if (page == startingPageIndex) null else page - 1,
                        nextKey = nextKey
                    )
                }
                TypeRequestDataMovie.MOVIE_NOW_PLAYING -> {
                    idRemoteKey = 3
                    val response = apiService.getNowPlayingMovies(page = page)
                    val movieEntity = response.results.toListMovieEntity()

                    val nextKey = if (movieEntity.isEmpty()) {
                        null
                    } else {
                        appDataBase.movieDao().insertMovies(movieEntity)
                        page + 1
                    }
                    appDataBase.remoteKeysDao().insert(
                        RemoteKeys(page, page - 1, nextKey, idRemoteKey)
                    )
                    LoadResult.Page(
                        data = movieEntity,
                        prevKey = if (page == startingPageIndex) null else page - 1,
                        nextKey = nextKey
                    )
                }
                TypeRequestDataMovie.MOVIE_TOP_RATED -> {
                    idRemoteKey = 4
                    val response = apiService.getTopRatedMovies(page = page)
                    val movieEntity = response.results.toListMovieEntity()

                    val nextKey = if (movieEntity.isEmpty()) {
                        null
                    } else {
                        appDataBase.movieDao().insertMovies(movieEntity)
                        page + 1
                    }
                    appDataBase.remoteKeysDao().insert(
                        RemoteKeys(page, page - 1, nextKey, idRemoteKey)
                    )
                    LoadResult.Page(
                        data = movieEntity,
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
    override fun getRefreshKey(state: PagingState<Int, MovieEntity>): Int? {
        // We use the remotekeys in database to get the number of page
        // the return will be sent to load() as params.key
        return appDataBase.remoteKeysDao().getRemoteKey(idRemoteKey)?.nextKey
    }
}