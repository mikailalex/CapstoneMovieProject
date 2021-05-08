package com.bumiayu.dicoding.capstonemovieproject.core.utils.ext

import com.bumiayu.dicoding.capstonemovieproject.core.data.source.local.entities.MovieDetailEntity
import com.bumiayu.dicoding.capstonemovieproject.core.data.source.local.entities.MovieEntity
import com.bumiayu.dicoding.capstonemovieproject.core.data.source.remote.response.GenresItemMovie
import com.bumiayu.dicoding.capstonemovieproject.core.data.source.remote.response.MovieDetailResponse
import com.bumiayu.dicoding.capstonemovieproject.core.data.source.remote.response.MovieItemSearch
import com.bumiayu.dicoding.capstonemovieproject.core.domain.model.movie.GenresMovie
import com.bumiayu.dicoding.capstonemovieproject.core.domain.model.movie.Movie
import com.bumiayu.dicoding.capstonemovieproject.core.domain.model.movie.MovieDetail

fun GenresItemMovie.toGenresMovie() : GenresMovie =
    GenresMovie(
        this.name,
        this.id
    )

fun GenresMovie.toGenresItemMovie() : GenresItemMovie =
    GenresItemMovie(
        this.name,
        this.id
    )

fun MovieEntity.toMovie(): Movie =
    Movie(
        this.id,
        this.title,
        this.score,
        this.imgPoster
    )

fun MovieDetailResponse.toMovieDetailEntity(): MovieDetailEntity =
    MovieDetailEntity(
        this.id,
        this.title,
        this.overview,
        this.genres,
        this.releaseDate,
        this.voteAverage,
        this.posterPath,
        this.backdropPath
    )

fun MovieDetailEntity?.toMovieDetail(): MovieDetail? {
    if(this != null){
        return MovieDetail(
            this.id,
            this.title,
            this.description,
            this.genres?.map { it.toGenresMovie() },
            this.releaseDate,
            this.score,
            this.imgPoster,
            this.imgBackground,
            this.isFavorite
        )
    }
    return null
}

fun MovieDetail.toMovieDetailEntity(): MovieDetailEntity =
    MovieDetailEntity(
        this.id,
        this.title,
        this.description,
        this.genres?.map { it.toGenresItemMovie() },
        this.releaseDate,
        this.score,
        this.imgPoster,
        this.imgBackground,
        this.isFavorite
    )

fun List<MovieItemSearch>.toListMovieEntity(): List<MovieEntity> =
    this.map {
        MovieEntity(
            it.id,
            it.title,
            it.voteAverage,
            it.posterPath
        )
    }
