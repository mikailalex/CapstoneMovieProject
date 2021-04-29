package com.bumiayu.dicoding.capstonemovieproject.core.domain.model.movie

data class Movie(
    val id: Int?,
    val title: String?,
    val score: Double?,
    val imgPoster: String?,
    var isFavorite: Boolean = false
)