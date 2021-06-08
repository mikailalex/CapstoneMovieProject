package com.bumiayu.dicoding.capstonemovieproject.core.domain.usecase

import androidx.paging.PagingData
import com.bumiayu.dicoding.capstonemovieproject.core.domain.model.Resource
import com.bumiayu.dicoding.capstonemovieproject.core.domain.model.movie.Movie
import com.bumiayu.dicoding.capstonemovieproject.core.domain.model.movie.MovieDetail
import kotlinx.coroutines.flow.Flow

interface MovieUseCase {
    fun getMovies(sortBy: String): Flow<PagingData<Movie>>
    fun getPopularMovies(): Flow<PagingData<Movie>>
    fun getNowPlayingMovies(): Flow<PagingData<Movie>>
    fun getTopRatedMovies(): Flow<PagingData<Movie>>
    fun getSearchMovies(query: String?): Flow<PagingData<Movie>>
    fun getDetailsMovie(movieId: Int): Flow<Resource<MovieDetail>>
    fun getFavoriteMovies(): Flow<PagingData<MovieDetail>>
    fun setFavoriteMovie(movie: MovieDetail)
}