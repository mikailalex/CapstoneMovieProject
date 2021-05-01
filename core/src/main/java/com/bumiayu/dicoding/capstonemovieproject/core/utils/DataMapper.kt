package com.bumiayu.dicoding.capstonemovieproject.core.utils

import com.bumiayu.dicoding.capstonemovieproject.core.data.source.local.entities.MovieDetailEntity
import com.bumiayu.dicoding.capstonemovieproject.core.data.source.local.entities.MovieEntity
import com.bumiayu.dicoding.capstonemovieproject.core.data.source.remote.response.MovieItemSearch
import com.bumiayu.dicoding.capstonemovieproject.core.domain.model.movie.Movie

object DataMapper {
    fun movieItemSearchToMovieEntityList(listItem: List<MovieItemSearch>): List<MovieEntity> =
        listItem.map {
            MovieEntity(
                it.id,
                it.title,
                it.voteAverage,
                it.posterPath
            )
        }

    fun movieDetailEntityToMovieList(listItem: List<MovieDetailEntity>): List<Movie> =
        listItem.map {
            Movie(
                it.id,
                it.title,
                it.score,
                it.imgPoster
            )
        }

    fun movieDetailEntityToMovie(item: MovieDetailEntity): Movie =
        Movie(
            item.id,
            item.title,
            item.score,
            item.imgPoster
        )

    fun movieEntityToMovie(item: MovieEntity): Movie =
        Movie(
            item.id,
            item.title,
            item.score,
            item.imgPoster
        )
}