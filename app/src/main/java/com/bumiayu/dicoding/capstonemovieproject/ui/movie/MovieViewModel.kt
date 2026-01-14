package com.bumiayu.dicoding.capstonemovieproject.ui.movie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.bumiayu.dicoding.capstonemovieproject.core.domain.model.Resource
import com.bumiayu.dicoding.capstonemovieproject.core.domain.model.movie.Movie
import com.bumiayu.dicoding.capstonemovieproject.core.domain.model.movie.MovieDetail
import com.bumiayu.dicoding.capstonemovieproject.core.domain.usecase.MovieUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*

class MovieViewModel(private val useCase: MovieUseCase) : ViewModel() {

    private lateinit var movie: MovieDetail

    private val searchQuery = MutableStateFlow("")

    fun setMovie(movie: MovieDetail) {
        this.movie = movie
    }

    fun setSearchQuery(query: String) {
        searchQuery.value = query
    }

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val searchMovies: Flow<PagingData<Movie>> = searchQuery
        .debounce(400)
        .filter { it.isNotEmpty() }
        .distinctUntilChanged()
        .flatMapLatest { query ->
            useCase.getSearchMovies(query)
        }
        .cachedIn(viewModelScope)

    val getMovies: Flow<PagingData<Movie>> = useCase.getMovies("Title").cachedIn(viewModelScope)

    val getPopularMovies: Flow<PagingData<Movie>> =
        useCase.getPopularMovies().cachedIn(viewModelScope)

    val getNowPlayingMovies: Flow<PagingData<Movie>> =
        useCase.getNowPlayingMovies().cachedIn(viewModelScope)

    val getTopRatedMovies: Flow<PagingData<Movie>> =
        useCase.getTopRatedMovies().cachedIn(viewModelScope)

    fun getDetailsMovie(movieId: Int): Flow<Resource<MovieDetail>> =
        useCase.getDetailsMovie(movieId)

    val getFavoriteMovies: Flow<PagingData<MovieDetail>> =
        useCase.getFavoriteMovies().cachedIn(viewModelScope)

    fun setFavoriteMovie() = useCase.setFavoriteMovie(movie)
}