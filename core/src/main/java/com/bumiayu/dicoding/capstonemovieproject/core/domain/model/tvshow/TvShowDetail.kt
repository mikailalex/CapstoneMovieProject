package com.bumiayu.dicoding.capstonemovieproject.core.domain.model.tvshow

data class TvShowDetail(
    val id: Int?,
    val title: String?,
    val description: String?,
    val genres: List<GenresTvShow>?,
    val releaseDate: String?,
    val score: Double?,
    val imgPoster: String?,
    val imgBackground: String?,
    var isFavorite: Boolean = false
)