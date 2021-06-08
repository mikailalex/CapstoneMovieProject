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
    @GET("search/movie")
    suspend fun getSearchMovies(
        @Query("api_key") apiKey: String = MOVIEDB_API_KEY,
        @Query("query") query: String?,
        @Query("page") page: Int? = 1
    ): MoviesResponse

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String = MOVIEDB_API_KEY,
        @Query("page") page: Int? = 1
    ): MoviesResponse

    @GET("movie/now_playing")
    suspend fun getNowPlayingMovies(
        @Query("api_key") apiKey: String = MOVIEDB_API_KEY,
        @Query("page") page: Int? = 1,
        @Query("region") region: String = "id"
    ): MoviesResponse

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("api_key") apiKey: String = MOVIEDB_API_KEY,
        @Query("page") page: Int? = 1
    ): MoviesResponse

    @GET("movie/{movie_id}")
    suspend fun getDetailMovie(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String = MOVIEDB_API_KEY
    ): MovieDetailResponse

    @GET("search/tv")
    suspend fun getSearchTvShows(
        @Query("api_key") apiKey: String = MOVIEDB_API_KEY,
        @Query("query") query: String?,
        @Query("page") page: Int? = 1
    ): TvShowsResponse

    @GET("tv/popular")
    suspend fun getPopularTvShows(
        @Query("api_key") apiKey: String = MOVIEDB_API_KEY,
        @Query("page") page: Int? = 1
    ): TvShowsResponse

    @GET("tv/on_the_air")
    suspend fun getOnTheAirTvShows(
        @Query("api_key") apiKey: String = MOVIEDB_API_KEY,
        @Query("page") page: Int? = 1,
        @Query("region") region: String = "id"
    ): TvShowsResponse

    @GET("tv/top_rated")
    suspend fun getTopRatedTvShows(
        @Query("api_key") apiKey: String = MOVIEDB_API_KEY,
        @Query("page") page: Int? = 1
    ): TvShowsResponse

    @GET("tv/{tv_id}")
    suspend fun getDetailTvShow(
        @Path("tv_id") tvId: Int,
        @Query("api_key") apiKey: String = MOVIEDB_API_KEY
    ): TvShowDetailResponse
}