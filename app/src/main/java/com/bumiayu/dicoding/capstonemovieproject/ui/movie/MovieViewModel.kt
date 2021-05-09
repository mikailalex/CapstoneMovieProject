package com.bumiayu.dicoding.capstonemovieproject.ui.movie

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.bumiayu.dicoding.capstonemovieproject.core.domain.model.Resource
import com.bumiayu.dicoding.capstonemovieproject.core.domain.model.movie.Movie
import com.bumiayu.dicoding.capstonemovieproject.core.domain.model.movie.MovieDetail
import com.bumiayu.dicoding.capstonemovieproject.core.domain.usecase.MovieUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@SuppressLint("CheckResult")
class MovieViewModel(private val useCase: MovieUseCase) : ViewModel() {

    private lateinit var movie: MovieDetail

    fun setMovie(movie: MovieDetail) {
        this.movie = movie
    }

    val getMovies: Flow<PagingData<Movie>> = useCase.getMovies("Title").cachedIn(viewModelScope)

    val getPopularMovies: Flow<PagingData<Movie>> = useCase.getPopularMovies().cachedIn(viewModelScope)

    val getNowPlayingMovies: Flow<PagingData<Movie>> = useCase.getNowPlayingMovies().cachedIn(viewModelScope)

    fun getSearchMovies(query: String): Flow<PagingData<Movie>> =
        useCase.getSearchMovies(query).cachedIn(viewModelScope)

    fun getDetailsMovie(movieId: Int): Flow<Resource<MovieDetail>> =
        useCase.getDetailsMovie(movieId)

    fun getFavoriteMovies(): Flow<PagingData<MovieDetail>> =
        useCase.getFavoriteMovies().cachedIn(viewModelScope)

    fun setFavoriteMovie() = useCase.setFavoriteMovie(movie)
}