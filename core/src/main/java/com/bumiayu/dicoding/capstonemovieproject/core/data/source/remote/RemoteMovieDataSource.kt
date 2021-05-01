package com.bumiayu.dicoding.capstonemovieproject.core.data.source.remote

import android.util.Log
import com.bumiayu.dicoding.capstonemovieproject.core.data.source.remote.network.ApiResponse
import com.bumiayu.dicoding.capstonemovieproject.core.data.source.remote.network.ApiService
import com.bumiayu.dicoding.capstonemovieproject.core.data.source.remote.response.MovieDetailResponse
import com.bumiayu.dicoding.capstonemovieproject.core.data.source.remote.response.MovieItemSearch
import com.bumiayu.dicoding.capstonemovieproject.core.utils.ext.EspressoIdlingResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class RemoteMovieDataSource(private val apiService: ApiService) {

    suspend fun getPopularMovies(): Flow<ApiResponse<List<MovieItemSearch>>> {
        EspressoIdlingResource.increment()
        return flow {
            try {
                Log.d("aaaaaa", "onRemote")
                val response = apiService.getPopularMovies()
                val data = response.results
                if (data.isNotEmpty()) {
                    emit(ApiResponse.Succes(data))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Log.e("RemoteDataSourceMovies", e.toString())
            } finally {
                EspressoIdlingResource.decrement()
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getSearchMovies(query: String?): Flow<ApiResponse<List<MovieItemSearch>>> {
        EspressoIdlingResource.increment()
        return flow {
            try {
                val response = apiService.getSearchMovies(query = query)
                val data = response.results
                if (data.isNotEmpty()) {
                    emit(ApiResponse.Succes(data))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Log.e("RemoteDataSourceMovies", e.toString())
            } finally {
                EspressoIdlingResource.decrement()
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getDetailsMovie(movieId: Int): Flow<ApiResponse<MovieDetailResponse>>{
        EspressoIdlingResource.increment()
        return flow {
            try {
                val response = apiService.getDetailMovie(movieId)
                emit(ApiResponse.Succes(response))
            } catch (e: Exception){
                emit(ApiResponse.Error(e.toString()))
                Log.e("RemoteDataSourceMovies", e.toString())
            } finally {
                EspressoIdlingResource.decrement()
            }
        }.flowOn(Dispatchers.IO)
    }
}