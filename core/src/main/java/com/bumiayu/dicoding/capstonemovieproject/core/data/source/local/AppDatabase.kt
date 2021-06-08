package com.bumiayu.dicoding.capstonemovieproject.core.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.bumiayu.dicoding.capstonemovieproject.core.data.source.local.entities.*
import com.bumiayu.dicoding.capstonemovieproject.core.data.source.local.room.MovieDao
import com.bumiayu.dicoding.capstonemovieproject.core.data.source.local.room.RemoteKeysDao
import com.bumiayu.dicoding.capstonemovieproject.core.data.source.local.room.TvShowDao

@Database(
    entities = [MovieEntity::class, MovieDetailEntity::class, TvShowEntity::class, TvShowDetailEntity::class, RemoteKeys::class],
    version = 1,
    exportSchema = false,
)
@TypeConverters(GenresMovieTypeConverter::class, GenresTvShowTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
    abstract fun tvShowDao(): TvShowDao
    abstract fun remoteKeysDao(): RemoteKeysDao
}