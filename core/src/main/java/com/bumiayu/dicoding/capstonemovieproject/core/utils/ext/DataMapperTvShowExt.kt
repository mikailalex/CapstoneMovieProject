package com.bumiayu.dicoding.capstonemovieproject.core.utils.ext

import com.bumiayu.dicoding.capstonemovieproject.core.data.source.local.entities.TvShowDetailEntity
import com.bumiayu.dicoding.capstonemovieproject.core.data.source.local.entities.TvShowEntity
import com.bumiayu.dicoding.capstonemovieproject.core.data.source.remote.response.GenresItemTvShow
import com.bumiayu.dicoding.capstonemovieproject.core.data.source.remote.response.TvShowDetailResponse
import com.bumiayu.dicoding.capstonemovieproject.core.data.source.remote.response.TvShowItemSearch
import com.bumiayu.dicoding.capstonemovieproject.core.domain.model.tvshow.GenresTvShow
import com.bumiayu.dicoding.capstonemovieproject.core.domain.model.tvshow.TvShow
import com.bumiayu.dicoding.capstonemovieproject.core.domain.model.tvshow.TvShowDetail

fun GenresItemTvShow.toGenresTvShow(): GenresTvShow =
    GenresTvShow(
        this.name,
        this.id
    )

fun GenresTvShow.toGenresItemTvShow(): GenresItemTvShow =
    GenresItemTvShow(
        this.name,
        this.id
    )

fun TvShowEntity.toTvShow(): TvShow =
    TvShow(
        this.id,
        this.title,
        this.score,
        this.imgPoster
    )

fun TvShowDetailResponse.toTvShowDetailEntity(): TvShowDetailEntity =
    TvShowDetailEntity(
        this.id,
        this.name,
        this.overview,
        this.genres,
        this.firstAirDate,
        this.voteAverage,
        this.posterPath,
        this.backdropPath
    )

fun TvShowDetailEntity?.toTvShowDetail(): TvShowDetail? {
    if (this != null) {
        return TvShowDetail(
            this.id,
            this.title,
            this.description,
            this.genres?.map { it.toGenresTvShow() },
            this.releaseDate,
            this.score,
            this.imgPoster,
            this.imgBackground,
            this.isFavorite
        )
    }
    return null
}

fun TvShowDetail.toTvShowDetailEntity(): TvShowDetailEntity =
    TvShowDetailEntity(
        this.id,
        this.title,
        this.description,
        this.genres?.map { it.toGenresItemTvShow() },
        this.releaseDate,
        this.score,
        this.imgPoster,
        this.imgBackground,
        this.isFavorite
    )

fun List<TvShowItemSearch>.toListTvShowEntity(): List<TvShowEntity> =
    this.map {
        TvShowEntity(
            it.id,
            it.name,
            it.voteAverage,
            it.posterPath
        )
    }