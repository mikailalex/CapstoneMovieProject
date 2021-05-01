package com.bumiayu.dicoding.capstonemovieproject.core.domain.usecase

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.bumiayu.dicoding.capstonemovieproject.core.data.Resource
import com.bumiayu.dicoding.capstonemovieproject.core.domain.model.movie.Movie
import com.bumiayu.dicoding.capstonemovieproject.core.domain.model.movie.MovieDetail
import kotlinx.coroutines.flow.Flow

interface MovieUseCase {
    fun getMovies(sort: String): Flow<Resource<PagingData<Movie>>>
    fun getSearchMovies(query: String?): Flow<Resource<PagingData<Movie>>>
    fun getDetailsMovie(movieId: Int): Flow<Resource<MovieDetail>>
    fun getFavoriteMovies(): LiveData<PagingData<MovieDetail>>
    fun setFavoriteMovie(movie: MovieDetail)
}