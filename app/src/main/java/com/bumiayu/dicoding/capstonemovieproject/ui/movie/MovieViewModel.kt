package com.bumiayu.dicoding.capstonemovieproject.ui.movie

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.bumiayu.dicoding.capstonemovieproject.core.data.Resource
import com.bumiayu.dicoding.capstonemovieproject.core.domain.model.movie.Movie
import com.bumiayu.dicoding.capstonemovieproject.core.domain.usecase.MovieUseCase
import kotlinx.coroutines.flow.Flow

class MovieViewModel(private val useCase: MovieUseCase): ViewModel() {

    fun getMovies(sort: String): Flow<Resource<PagingData<Movie>>> =
        useCase.getMovies(sort)
}