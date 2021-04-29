package com.bumiayu.dicoding.capstonemovieproject.core.domain.model.tvshow

data class TvShow(
    val id: Int?,
    val title: String?,
    val score: Double?,
    val imgPoster: String?,
    var isFavorite: Boolean = false
)