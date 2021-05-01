package com.bumiayu.dicoding.capstonemovieproject.core.data.source.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKeys(
    @PrimaryKey
    val currentKey: Int,
    val prevKey: Int?,
    val nextKey: Int?
)
