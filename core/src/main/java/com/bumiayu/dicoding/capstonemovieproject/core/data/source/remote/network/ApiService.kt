package com.bumiayu.dicoding.capstonemovieproject.core.data.source.remote.network

import com.bumiayu.dicoding.capstonemovieproject.core.BuildConfig.MOVIEDB_API_KEY
import com.bumiayu.dicoding.capstonemovieproject.core.data.source.remote.response.MovieDetailResponse
import com.bumiayu.dicoding.capstonemovieproject.core.data.source.remote.response.MoviesResponse
import com.bumiayu.dicoding.capstonemovieproject.core.data.source.remote.response.TvShowDetailResponse
import com.bumiayu.dicoding.capstonemovieproject.core.data.source.remote.response.TvShowsResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String = MOVIEDB_API_KEY,
        @Query("page") page: Int? = 1
    ): MoviesResponse

    @GET("search/movie")
    suspend fun getSearchMovies(
        @Query("api_key") apiKey: String = MOVIEDB_API_KEY,
        @Query("query") query: String?
    ): MoviesResponse

    @GET("movie/{movie_id}")
    fun getDetailMovie(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String = MOVIEDB_API_KEY
    ): MovieDetailResponse

    @GET("tv/popular")
    fun getPopularTvShows(
        @Query("api_key") apiKey: String = MOVIEDB_API_KEY,
        @Query("page") page: Int? = 1
    ): TvShowsResponse

    @GET("search/tv")
    fun getSearchTvShows(
        @Query("api_key") apiKey: String = MOVIEDB_API_KEY,
        @Query("query") query: String?
    ): TvShowsResponse

    @GET("tv/{tv_id}")
    fun getDetailsTvShow(
        @Path("tv_id") tvId: Int,
        @Query("api_key") apiKey: String = MOVIEDB_API_KEY
    ): TvShowDetailResponse
}