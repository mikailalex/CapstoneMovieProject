package com.bumiayu.dicoding.capstonemovieproject.core.data.source

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.bumiayu.dicoding.capstonemovieproject.core.data.source.local.room.AppDatabase
import com.bumiayu.dicoding.capstonemovieproject.core.data.source.remote.RemoteMoviePagingDataSource
import com.bumiayu.dicoding.capstonemovieproject.core.data.source.remote.network.ApiResponse
import com.bumiayu.dicoding.capstonemovieproject.core.data.source.remote.network.ApiService
import com.bumiayu.dicoding.capstonemovieproject.core.data.source.remote.response.MovieDetailResponse
import com.bumiayu.dicoding.capstonemovieproject.core.domain.model.Resource
import com.bumiayu.dicoding.capstonemovieproject.core.domain.model.movie.Movie
import com.bumiayu.dicoding.capstonemovieproject.core.domain.model.movie.MovieDetail
import com.bumiayu.dicoding.capstonemovieproject.core.domain.model.tvshow.TvShowDetail
import com.bumiayu.dicoding.capstonemovieproject.core.domain.repository.IMovieRepository
import com.bumiayu.dicoding.capstonemovieproject.core.utils.SortUtils
import com.bumiayu.dicoding.capstonemovieproject.core.utils.SortUtils.MOVIES_TABLE
import com.bumiayu.dicoding.capstonemovieproject.core.utils.ext.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.IOException

class MovieRepository(
    private val appDatabase: AppDatabase,
    private val apiService: ApiService
) : IMovieRepository {

    // Offline (get data from popular and now playing)
    override fun getMovies(sortBy: String): Flow<PagingData<Movie>> =
        Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false, maxSize = 60),
            pagingSourceFactory = {
                appDatabase.movieDao().getMovies(SortUtils.getSortedQuery(sortBy, MOVIES_TABLE))
            }
        ).flow.map { pagingData ->
            pagingData.map {
                it.toMovie()
            }
        }

    // Online
    override fun getPopularMovies(): Flow<PagingData<Movie>> =
        Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false, maxSize = 60),
            pagingSourceFactory = {
                RemoteMoviePagingDataSource(
                    TypeRequestDataMovie.MOVIE_POPULAR,
                    apiService,
                    appDatabase
                )
            }
        ).flow.map { pagingData ->
            pagingData.map {
                it.toMovie()
            }
        }

    // Online
    override fun getNowPlayingMovies(): Flow<PagingData<Movie>> =
        Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false, maxSize = 60),
            pagingSourceFactory = {
                RemoteMoviePagingDataSource(
                    TypeRequestDataMovie.MOVIE_NOW_PLAYING,
                    apiService,
                    appDatabase
                )
            }
        ).flow.map { pagingData ->
            pagingData.map {
                it.toMovie()
            }
        }

    // Online
    override fun getSearchMovies(query: String?): Flow<PagingData<Movie>> =
        Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false, maxSize = 60),
            pagingSourceFactory = {
                RemoteMoviePagingDataSource(
                    TypeRequestDataMovie.MOVIE_SEARCH,
                    apiService,
                    appDatabase,
                    query
                )
            }
        ).flow.map { pagingData ->
            pagingData.map {
                it.toMovie()
            }
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
                        emit(ApiResponse.Error(e.message.toString())
                        )
                    }
                }

            override suspend fun saveCallResult(data: MovieDetailResponse) =
                appDatabase.movieDao().insertDetailMovie(data.toMovieDetailEntity())

        }.asFlow()

//        flow {
//            appDatabase.movieDao().getDetailMovieById(movieId)?.collectLatest {
//                Log.d("000repomovdataDb", it.toString())
//                if (it != null) {
//                    emit(Resource.Success(it.toMovieDetail()))
//                } else {
//                    try {
//                        val response = apiService.getDetailMovie(movieId).toMovieDetailEntity()
//                        appDatabase.movieDao().insertDetailMovie(response)
//                        emit(Resource.Success(response.toMovieDetail()))
//                    } catch (e: Exception) {
//                        emit(Resource.Error<MovieDetail>(e.toString()))
//                    }
//                }
//            }
//        }.flowOn(Dispatchers.IO)
    // Offline
    override fun getFavoriteMovies(): Flow<PagingData<MovieDetail>> =
        Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false, maxSize = 60),
            pagingSourceFactory = { appDatabase.movieDao().getListFavoriteMovies() }
        ).flow.map { pagingData ->
            pagingData.map {
                it.toMovieDetail()!!
            }
        }

    // Offline
    override fun setFavoriteMovie(movie: MovieDetail) {
        val movieDetailEntity = movie.toMovieDetailEntity()
        movieDetailEntity.isFavorite = !movieDetailEntity.isFavorite
        CoroutineScope(Dispatchers.IO).launch {
            appDatabase.movieDao().updateDetailMovie(movieDetailEntity)
        }
    }
}