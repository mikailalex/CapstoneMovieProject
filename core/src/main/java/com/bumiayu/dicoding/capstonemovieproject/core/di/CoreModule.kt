package com.bumiayu.dicoding.capstonemovieproject.core.di

import androidx.paging.ExperimentalPagingApi
import androidx.room.Room
import com.bumiayu.dicoding.capstonemovieproject.core.BuildConfig.BASE_URL
import com.bumiayu.dicoding.capstonemovieproject.core.data.source.MovieRepository
import com.bumiayu.dicoding.capstonemovieproject.core.data.source.local.LocalMovieDataSource
import com.bumiayu.dicoding.capstonemovieproject.core.data.source.local.room.AppDatabase
import com.bumiayu.dicoding.capstonemovieproject.core.data.source.remote.RemoteMovieDataSource
import com.bumiayu.dicoding.capstonemovieproject.core.data.source.remote.network.ApiService
import com.bumiayu.dicoding.capstonemovieproject.core.domain.repository.IMovieRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val databaseModule = module {
    factory { get<AppDatabase>().movieDao() }
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java, "App.db"
        ).fallbackToDestructiveMigration().build()
    }
}

val networkModule = module {
    single {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .build()
    }
    single {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
        retrofit.create(ApiService::class.java)
    }
}

@ExperimentalPagingApi
val repositoryModule = module {
    single { LocalMovieDataSource(get()) }
    single { RemoteMovieDataSource(get()) }
    single<IMovieRepository> { MovieRepository(get(), get()) }
}