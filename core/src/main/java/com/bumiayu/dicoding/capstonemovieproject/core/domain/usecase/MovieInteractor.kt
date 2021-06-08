package com.bumiayu.dicoding.capstonemovieproject.core.domain.usecase

import androidx.paging.PagingData
import com.bumiayu.dicoding.capstonemovieproject.core.domain.model.Resource
import com.bumiayu.dicoding.capstonemovieproject.core.domain.model.movie.Movie
import com.bumiayu.dicoding.capstonemovieproject.core.domain.model.movie.MovieDetail
import com.bumiayu.dicoding.capstonemovieproject.core.domain.repository.IMovieRepository
import kotlinx.coroutines.flow.Flow

class MovieInteractor(private val movieRepository: IMovieRepository) : MovieUseCase {
    override fun getMovies(sortBy: String): Flow<PagingData<Movie>> = movieRepository.getMovies(sortBy)

    override fun getPopularMovies(): Flow<PagingData<Movie>> = movieRepository.getPopularMovies()

    override fun getNowPlayingMovies(): Flow<PagingData<Movie>> = movieRepository.getNowPlayingMovies()

    override fun getTopRatedMovies(): Flow<PagingData<Movie>> = movieRepository.getTopRatedMovies()

    override fun getSearchMovies(query: String?): Flow<PagingData<Movie>> = movieRepository.getSearchMovies(query)

    override fun getDetailsMovie(movieId: Int): Flow<Resource<MovieDetail>> = movieRepository.getDetailsMovie(movieId)

    override fun getFavoriteMovies(): Flow<PagingData<MovieDetail>> = movieRepository.getFavoriteMovies()

    override fun setFavoriteMovie(movie: MovieDetail) = movieRepository.setFavoriteMovie(movie)
}