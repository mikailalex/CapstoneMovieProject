package com.bumiayu.dicoding.capstonemovieproject.core.data.source

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.bumiayu.dicoding.capstonemovieproject.core.data.Resource
import com.bumiayu.dicoding.capstonemovieproject.core.data.source.local.LocalMovieDataSource
import com.bumiayu.dicoding.capstonemovieproject.core.data.source.remote.RemoteMovieDataSource
import com.bumiayu.dicoding.capstonemovieproject.core.data.source.remote.network.ApiResponse
import com.bumiayu.dicoding.capstonemovieproject.core.data.source.remote.response.MovieItemSearch
import com.bumiayu.dicoding.capstonemovieproject.core.domain.model.movie.Movie
import com.bumiayu.dicoding.capstonemovieproject.core.domain.model.movie.MovieDetail
import com.bumiayu.dicoding.capstonemovieproject.core.domain.repository.IMovieRepository
import com.bumiayu.dicoding.capstonemovieproject.core.utils.DataMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MovieRepository @ExperimentalPagingApi constructor(
    private val localMovieDataSource: LocalMovieDataSource,
    private val remoteMovieDataSource: RemoteMovieDataSource
) : IMovieRepository {
    override fun getMovies(sort: String): Flow<Resource<PagingData<Movie>>> =
        object : NetworkBoundResource<PagingData<Movie>, List<MovieItemSearch>>() {
            override fun loadFromDB(): Flow<PagingData<Movie>> =
                Pager(
                    config = PagingConfig(pageSize = 20, enablePlaceholders = false),
                    pagingSourceFactory = { localMovieDataSource.getMovies(sort) }
                ).flow.map { pagingData -> pagingData.map {
                    DataMapper.movieEntityToMovie(it) } }

            override fun shouldFetch(data: PagingData<Movie>?): Boolean = data == null

            override suspend fun createCall(): Flow<ApiResponse<List<MovieItemSearch>>> =
                remoteMovieDataSource.getPopularMovies()

            override suspend fun saveCallResult(data: List<MovieItemSearch>) {
                DataMapper.movieItemSearchToMovieEntityList(data).let {
                    localMovieDataSource.insertMovies(it)
                }
            }
        }.asFlow()

    override fun getSearchMovies(query: String?): Flow<Resource<PagingData<Movie>>> {
        TODO("Not yet implemented")
    }

    override fun getDetailsMovie(movieId: Int): Flow<Resource<MovieDetail>> {
        TODO("Not yet implemented")
    }

    override fun getFavoriteMovies(): LiveData<PagingData<MovieDetail>> {
        TODO("Not yet implemented")
    }

    override fun setFavoriteMovie(movie: MovieDetail) {
        TODO("Not yet implemented")
    }
}