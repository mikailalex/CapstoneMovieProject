package com.bumiayu.dicoding.capstonemovieproject.core.data.source.local.entities

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tb_list_movies")
data class MovieEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    val id: Int?,
    @ColumnInfo(name = "title")
    val title: String?,
    @ColumnInfo(name = "score")
    val score: Double?,
    @ColumnInfo(name = "img_poster")
    val imgPoster: String?,
    @NonNull
    @ColumnInfo(name = "isFavorite")
    var isFavorite: Boolean = false
)