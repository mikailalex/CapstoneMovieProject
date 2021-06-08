package com.bumiayu.dicoding.capstonemovieproject.core.data.source.local.room

import androidx.paging.PagingSource
import androidx.room.*
import androidx.sqlite.db.SimpleSQLiteQuery
import com.bumiayu.dicoding.capstonemovieproject.core.data.source.local.entities.MovieDetailEntity
import com.bumiayu.dicoding.capstonemovieproject.core.data.source.local.entities.MovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    @RawQuery(observedEntities = [MovieEntity::class])
    fun getMovies(query: SimpleSQLiteQuery): PagingSource<Int, MovieEntity>

    @Query("SELECT * FROM tb_detail_movie WHERE isFavorite = 1")
    fun getListFavoriteMovies(): PagingSource<Int, MovieDetailEntity>

    @Query("SELECT * FROM tb_list_movies WHERE title LIKE (:query) ORDER BY title")
    fun getListSearchMovies(query: String?): PagingSource<Int, MovieEntity>

    @Query("SELECT * FROM tb_detail_movie WHERE id = :id")
    fun getDetailMovieById(id: Int): Flow<MovieDetailEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE, entity = MovieEntity::class)
    suspend fun insertMovies(movies: List<MovieEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE, entity = MovieDetailEntity::class)
    suspend fun insertDetailMovie(movie: MovieDetailEntity)

    @Update(entity = MovieDetailEntity::class)
    suspend fun updateDetailMovie(movie: MovieDetailEntity)
}