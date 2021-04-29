package com.bumiayu.dicoding.capstonemovieproject.core.data.source

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.bumiayu.dicoding.capstonemovieproject.core.data.Resource
import com.bumiayu.dicoding.capstonemovieproject.core.data.source.local.LocalMovieDataSource
import com.bumiayu.dicoding.capstonemovieproject.core.data.source.remote.RemoteMovieDataSource
import com.bumiayu.dicoding.capstonemovieproject.core.domain.model.movie.Movie
import com.bumiayu.dicoding.capstonemovieproject.core.domain.model.movie.MovieDetail
import com.bumiayu.dicoding.capstonemovieproject.core.domain.repository.IMovieRepository
import kotlinx.coroutines.flow.Flow

class MovieRepository(
    private val localMovieDataSource: LocalMovieDataSource,
    private val remoteMovieDataSource: RemoteMovieDataSource
) : IMovieRepository {
    override fun getMovies(sort: String): Flow<Resource<PagedList<Movie>>> {
        TODO("Not yet implemented")
    }

    override fun getSearchMovies(query: String?): Flow<Resource<PagedList<Movie>>> {
        TODO("Not yet implemented")
    }

    override fun getDetailsMovie(movieId: Int): Flow<Resource<MovieDetail>> {
        TODO("Not yet implemented")
    }

    override fun getFavoriteMovies(): LiveData<PagedList<MovieDetail>> {
        TODO("Not yet implemented")
    }

    override fun setFavoriteMovie(movie: MovieDetail) {
        TODO("Not yet implemented")
    }
}