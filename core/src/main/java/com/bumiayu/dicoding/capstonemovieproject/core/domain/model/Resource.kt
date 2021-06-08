package com.bumiayu.dicoding.capstonemovieproject.core.domain.model

// Use this class to inform load data Succes, error, etc
// The PagingData didn't need this since it is own LoadState built in
// This class used to get result of detail item (Movie and TvShow)
sealed class Resource<T>(val data: T? = null, val message: String? = null) {
    class Loading<T>(data: T? = null) : Resource<T>(data)
    class Success<T>(data: T?) : Resource<T>(data)
    class Error<T>(message: String?, data: T? = null) : Resource<T>(data, message)
}