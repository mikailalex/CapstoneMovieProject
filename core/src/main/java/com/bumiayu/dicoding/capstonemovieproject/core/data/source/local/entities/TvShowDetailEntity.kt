package com.bumiayu.dicoding.capstonemovieproject.core.data.source.local.entities

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.bumiayu.dicoding.capstonemovieproject.core.data.source.remote.response.GenresItemTvShow
import com.google.gson.Gson

@Entity(tableName = "tb_detail_tvshow")
data class TvShowDetailEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    val id: Int?,
    @ColumnInfo(name = "title")
    val title: String?,
    @ColumnInfo(name = "description")
    val description: String?,
    @ColumnInfo(name = "genre")
    val genres: List<GenresItemTvShow>?,
    @ColumnInfo(name = "release_date")
    val releaseDate: String?,
    @ColumnInfo(name = "score")
    val score: Double?,
    @ColumnInfo(name = "img_poster")
    val imgPoster: String?,
    @ColumnInfo(name = "img_background")
    val imgBackground: String?,
    @NonNull
    @ColumnInfo(name = "isFavorite")
    var isFavorite: Boolean = false
)

class GenresTvShowTypeConverter {
    @TypeConverter
    fun listToJson(value: List<GenresItemTvShow>?): String = Gson().toJson(value)

    @TypeConverter
    fun jsonToList(value: String) =
        Gson().fromJson(value, Array<GenresItemTvShow>::class.java).toList()
}