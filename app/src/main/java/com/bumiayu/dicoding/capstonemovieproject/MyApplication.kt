package com.bumiayu.dicoding.capstonemovieproject

import android.app.Application
import androidx.paging.ExperimentalPagingApi
import com.bumiayu.dicoding.capstonemovieproject.core.di.coreModules
import com.bumiayu.dicoding.capstonemovieproject.di.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApplication: Application() {
    @OptIn(ExperimentalPagingApi::class)
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MyApplication)
            modules(appModules + coreModules)
        }
    }
}