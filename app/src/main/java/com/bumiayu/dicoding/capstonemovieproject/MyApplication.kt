package com.bumiayu.dicoding.capstonemovieproject

import android.app.Application
import androidx.paging.ExperimentalPagingApi
import com.bumiayu.dicoding.capstonemovieproject.core.di.databaseModule
import com.bumiayu.dicoding.capstonemovieproject.core.di.networkModule
import com.bumiayu.dicoding.capstonemovieproject.core.di.repositoryModule
import com.bumiayu.dicoding.capstonemovieproject.di.useCaseModule
import com.bumiayu.dicoding.capstonemovieproject.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApplication: Application() {
    @OptIn(ExperimentalPagingApi::class)
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MyApplication)
            modules(
                databaseModule,
                networkModule,
                repositoryModule,
                useCaseModule,
                viewModelModule
            )
        }
    }
}