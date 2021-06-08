package com.bumiayu.dicoding.capstonemovieproject.core.data.source.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKeys(
    val currentKey: Int,
    val prevKey: Int?,
    val nextKey: Int?,
    @PrimaryKey(autoGenerate = false)
    // id 1 for Search Movie
    // id 2 for Popular Movie
    // id 3 for Now Playing Movie
    // id 4 for Top Rated Movie
    // id 5 for Search Tv Show
    // id 6 for Popular Tv Shows
    // id 7 for On The Air Tv Shows
    // id 8 for Top Rated Tv Shows
    val id: Int
)
