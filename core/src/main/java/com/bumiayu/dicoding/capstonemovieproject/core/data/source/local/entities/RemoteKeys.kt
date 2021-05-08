package com.bumiayu.dicoding.capstonemovieproject.core.data.source.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKeys(
    val currentKey: Int,
    val prevKey: Int?,
    val nextKey: Int?,
    @PrimaryKey(autoGenerate = false)
    // id 1 for Popular Movie
    // id 2 for Now Playing Movie
    // id 3 for Search Movie
    // id 4 for Popular Tv Shows
    // id 5 for Tv Shows On The Air
    // id 6 for Search Tv Show
    val id: Int
)
