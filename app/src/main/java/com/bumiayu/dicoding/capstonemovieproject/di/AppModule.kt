package com.bumiayu.dicoding.capstonemovieproject.di

import com.bumiayu.dicoding.capstonemovieproject.core.domain.usecase.MovieInteractor
import com.bumiayu.dicoding.capstonemovieproject.core.domain.usecase.MovieUseCase
import com.bumiayu.dicoding.capstonemovieproject.core.domain.usecase.TvShowInteractor
import com.bumiayu.dicoding.capstonemovieproject.core.domain.usecase.TvShowUseCase
import com.bumiayu.dicoding.capstonemovieproject.ui.movie.MovieViewModel
import com.bumiayu.dicoding.capstonemovieproject.ui.tv.TvShowViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val useCaseModule = module {
    single<MovieUseCase> { MovieInteractor(get()) }
    single<TvShowUseCase> { TvShowInteractor(get()) }
}

val viewModelModule = module {
    viewModel { MovieViewModel(get()) }
    viewModel { TvShowViewModel(get()) }
}

val appModules = listOf(useCaseModule, viewModelModule)