package com.bumiayu.dicoding.capstonemovieproject.core.domain.model.movie

data class MovieDetail(
    val id: Int?,
    val title: String?,
    val description: String?,
    val genres: List<GenresMovie>?,
    val releaseDate: String?,
    val score: Double?,
    val imgPoster: String?,
    val imgBackground: String?,
    var isFavorite: Boolean = false
)