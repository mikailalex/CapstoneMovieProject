package com.bumiayu.dicoding.capstonemovieproject.core.data

sealed class Resource<T>(val data: T? = null, val message: String? = null) {
    class Loading<T>(data: T? = null) : Resource<T>(data)
    class Succes<T>(data: T?) : Resource<T>(data)
    class Empty<T>() : Resource<T>()
    class Error<T>(message: String?, data: T? = null) : Resource<T>(data, message)
}