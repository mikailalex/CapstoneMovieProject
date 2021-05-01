package com.bumiayu.dicoding.capstonemovieproject.di

import com.bumiayu.dicoding.capstonemovieproject.core.domain.usecase.MovieInteractor
import com.bumiayu.dicoding.capstonemovieproject.core.domain.usecase.MovieUseCase
import com.bumiayu.dicoding.capstonemovieproject.ui.movie.MovieViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val useCaseModule = module {
    factory<MovieUseCase> { MovieInteractor(get()) }
}

val viewModelModule = module {
    viewModel { MovieViewModel(get()) }
}