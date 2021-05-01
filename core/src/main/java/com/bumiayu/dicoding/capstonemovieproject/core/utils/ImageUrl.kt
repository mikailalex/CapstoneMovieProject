package com.bumiayu.dicoding.capstonemovieproject.core.utils

object ImageUrl {
    fun poster(urlPath: String?): String {
        val baseUrl = "https://image.tmdb.org/t/p/w200"
        return baseUrl.plus(urlPath)
    }

    fun posterBig(urlPath: String?): String {
        val baseUrl = "https://image.tmdb.org/t/p/original"
        return baseUrl.plus(urlPath)
    }

    fun background(urlPath: String?): String {
        val baseUrl = "https://image.tmdb.org/t/p/w400"
        return baseUrl.plus(urlPath)
    }
}