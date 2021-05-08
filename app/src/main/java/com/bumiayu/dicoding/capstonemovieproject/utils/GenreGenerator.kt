package com.bumiayu.dicoding.capstonemovieproject.utils

import com.bumiayu.dicoding.capstonemovieproject.core.domain.model.movie.GenresMovie
import com.bumiayu.dicoding.capstonemovieproject.core.domain.model.tvshow.GenresTvShow

object GenreGenerator {
    @JvmName("create1")
    fun create(genres: List<GenresMovie>?): String {
        if (genres.isNullOrEmpty()) return "Empty"
        val stringBuilder = StringBuilder()
        var i = 0
        for (genre in genres) {
            stringBuilder.append(genre.name)
            i++
            if (i == genres.size) stringBuilder.append(".") else stringBuilder.append(", ")
        }
        return stringBuilder.toString()
    }

    fun create(genres: List<GenresTvShow>?): String {
        if (genres.isNullOrEmpty()) return "Empty"
        val stringBuilder = StringBuilder()
        var i = 0
        for (genre in genres) {
            stringBuilder.append(genre.name)
            i++
            if (i == genres.size) stringBuilder.append(".") else stringBuilder.append(", ")
        }
        return stringBuilder.toString()
    }
}