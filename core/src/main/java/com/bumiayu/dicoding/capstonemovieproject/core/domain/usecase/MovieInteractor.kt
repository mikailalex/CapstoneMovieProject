package com.bumiayu.dicoding.capstonemovieproject.core.domain.usecase

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.bumiayu.dicoding.capstonemovieproject.core.data.Resource
import com.bumiayu.dicoding.capstonemovieproject.core.domain.model.movie.Movie
import com.bumiayu.dicoding.capstonemovieproject.core.domain.model.movie.MovieDetail
import com.bumiayu.dicoding.capstonemovieproject.core.domain.repository.IMovieRepository
import kotlinx.coroutines.flow.Flow

class MovieInteractor(private val movieRepository: IMovieRepository) : MovieUseCase {
    override fun getMovies(sort: String): Flow<Resource<PagingData<Movie>>> = movieRepository.getMovies(sort)

    override fun getSearchMovies(query: String?): Flow<Resource<PagingData<Movie>>> = movieRepository.getSearchMovies(query)

    override fun getDetailsMovie(movieId: Int): Flow<Resource<MovieDetail>> = movieRepository.getDetailsMovie(movieId)

    override fun getFavoriteMovies(): LiveData<PagingData<MovieDetail>> = movieRepository.getFavoriteMovies()

    override fun setFavoriteMovie(movie: MovieDetail) = movieRepository.setFavoriteMovie(movie)
}