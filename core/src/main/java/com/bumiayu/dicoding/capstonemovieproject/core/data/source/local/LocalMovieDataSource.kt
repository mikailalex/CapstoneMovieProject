package com.bumiayu.dicoding.capstonemovieproject.core.data.source.local

import androidx.paging.DataSource
import androidx.paging.PagingSource
import com.bumiayu.dicoding.capstonemovieproject.core.data.source.local.entities.MovieDetailEntity
import com.bumiayu.dicoding.capstonemovieproject.core.data.source.local.entities.MovieEntity
import com.bumiayu.dicoding.capstonemovieproject.core.data.source.local.room.MovieDao
import com.bumiayu.dicoding.capstonemovieproject.core.utils.SortUtils
import com.bumiayu.dicoding.capstonemovieproject.core.utils.SortUtils.MOVIE_ENTITIES

class LocalMovieDataSource(private val mMovieDao: MovieDao) {

    fun getMovies(sort: String): PagingSource<Int, MovieEntity> =
        mMovieDao.getMovies(SortUtils.getSortedQuery(sort, MOVIE_ENTITIES))

    fun getListFavoriteMovies(): PagingSource<Int, MovieDetailEntity> =
        mMovieDao.getListFavoriteMovies()

    fun getListSearchMovies(query: String?): PagingSource<Int, MovieEntity> =
        mMovieDao.getListSearchMovies(query)

    fun getDetailMovieById(id: Int): MovieDetailEntity =
        mMovieDao.getDetailMovieById(id)

    suspend fun insertMovies(movies: List<MovieEntity>) =
        mMovieDao.insertMovies(movies)

    suspend fun insertDetailMovie(movie: MovieDetailEntity) =
        mMovieDao.insertDetailMovie(movie)

    fun setFavoriteMovie(movie: MovieDetailEntity) {
        movie.isFavorite = !movie.isFavorite
        mMovieDao.updateDetailMovie(movie)
    }
}