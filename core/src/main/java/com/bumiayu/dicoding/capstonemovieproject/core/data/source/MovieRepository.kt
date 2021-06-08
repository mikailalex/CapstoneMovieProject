package com.bumiayu.dicoding.capstonemovieproject.core.data.source

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.bumiayu.dicoding.capstonemovieproject.core.data.source.local.AppDatabase
import com.bumiayu.dicoding.capstonemovieproject.core.data.source.remote.RemoteMoviePagingDataSource
import com.bumiayu.dicoding.capstonemovieproject.core.data.source.remote.network.ApiResponse
import com.bumiayu.dicoding.capstonemovieproject.core.data.source.remote.network.ApiService
import com.bumiayu.dicoding.capstonemovieproject.core.data.source.remote.response.MovieDetailResponse
import com.bumiayu.dicoding.capstonemovieproject.core.domain.model.Resource
import com.bumiayu.dicoding.capstonemovieproject.core.domain.model.movie.Movie
import com.bumiayu.dicoding.capstonemovieproject.core.domain.model.movie.MovieDetail
import com.bumiayu.dicoding.capstonemovieproject.core.domain.repository.IMovieRepository
import com.bumiayu.dicoding.capstonemovieproject.core.utils.SortUtils
import com.bumiayu.dicoding.capstonemovieproject.core.utils.SortUtils.MOVIES_TABLE
import com.bumiayu.dicoding.capstonemovieproject.core.utils.ext.toMovie
import com.bumiayu.dicoding.capstonemovieproject.core.utils.ext.toMovieDetail
import com.bumiayu.dicoding.capstonemovieproject.core.utils.ext.toMovieDetailEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.io.IOException

class MovieRepository(
    private val appDatabase: AppDatabase,
    private val apiService: ApiService
) : IMovieRepository {

    // Offline (get data from popular and now playing)
    override fun getMovies(sortBy: String): Flow<PagingData<Movie>> =
        Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false, maxSize = 120),
            pagingSourceFactory = {
                appDatabase.movieDao().getMovies(SortUtils.getSortedQuery(sortBy, MOVIES_TABLE))
            }
        ).flow.map { pagingData ->
            pagingData.map { it.toMovie() }
        }

    // Online
    override fun getPopularMovies(): Flow<PagingData<Movie>> =
        Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false, maxSize = 120),
            pagingSourceFactory = {
                RemoteMoviePagingDataSource(
                    TypeRequestDataMovie.MOVIE_POPULAR,
                    apiService,
                    appDatabase
                )
            }
        ).flow.map { pagingData ->
            pagingData.map { it.toMovie() }
        }

    // Online
    override fun getNowPlayingMovies(): Flow<PagingData<Movie>> =
        Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false, maxSize = 120),
            pagingSourceFactory = {
                RemoteMoviePagingDataSource(
                    TypeRequestDataMovie.MOVIE_NOW_PLAYING,
                    apiService,
                    appDatabase
                )
            }
        ).flow.map { pagingData ->
            pagingData.map { it.toMovie() }
        }

    // Online
    override fun getTopRatedMovies(): Flow<PagingData<Movie>> =
        Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false, maxSize = 120),
            pagingSourceFactory = {
                RemoteMoviePagingDataSource(
                    TypeRequestDataMovie.MOVIE_TOP_RATED,
                    apiService,
                    appDatabase
                )
            }
        ).flow.map { pagingData ->
            pagingData.map { it.toMovie() }
        }

    // Online
    override fun getSearchMovies(query: String?): Flow<PagingData<Movie>> =
        Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false, maxSize = 120),
            pagingSourceFactory = {
                RemoteMoviePagingDataSource(
                    TypeRequestDataMovie.MOVIE_SEARCH,
                    apiService,
                    appDatabase,
                    query
                )
            }
        ).flow.map { pagingData ->
            pagingData.map { it.toMovie() }
        }

    // Online and Offline
    override fun getDetailsMovie(movieId: Int): Flow<Resource<MovieDetail>> =
        object : NetworkBoundResource<MovieDetail, MovieDetailResponse>() {
            override fun loadFromDB(): Flow<MovieDetail?> =
                appDatabase.movieDao().getDetailMovieById(movieId).map {
                    it.toMovieDetail()
                }

            override fun shouldFetch(data: MovieDetail?): Boolean = data == null

            override suspend fun createCall(): Flow<ApiResponse<MovieDetailResponse>> =
                flow {
                    try {
                        val response = apiService.getDetailMovie(movieId)
                        emit(ApiResponse.Success(response))
                    } catch (e: IOException) {
                        e.printStackTrace()
                        emit(ApiResponse.Error(e.message.toString()))
                    }
                }

            override suspend fun saveCallResult(data: MovieDetailResponse) =
                appDatabase.movieDao().insertDetailMovie(data.toMovieDetailEntity())

        }.asFlow()

    // Offline
    override fun getFavoriteMovies(): Flow<PagingData<MovieDetail>> =
        Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false, maxSize = 120),
            pagingSourceFactory = { appDatabase.movieDao().getListFavoriteMovies() }
        ).flow.map { pagingData ->
            pagingData.map { it.toMovieDetail()!! }
        }

    // Offline
    override fun setFavoriteMovie(movie: MovieDetail) {
        CoroutineScope(Dispatchers.IO).launch {
            movie.isFavorite = !movie.isFavorite
            appDatabase.movieDao().updateDetailMovie(movie.toMovieDetailEntity())
        }
    }
}