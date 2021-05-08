package com.bumiayu.dicoding.capstonemovieproject.core.data.source.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bumiayu.dicoding.capstonemovieproject.core.data.source.local.entities.RemoteKeys

@Dao
interface RemoteKeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(remoteKey: RemoteKeys)

    @Query("SELECT * FROM remote_keys")
    suspend fun getRemoteKeys(): RemoteKeys?

    @Query("SELECT * FROM remote_keys WHERE id = :id ")
    fun getRemoteKey(id: Int): RemoteKeys?

    @Query("DELETE FROM remote_keys")
    suspend fun clearRemoteKeys()
}