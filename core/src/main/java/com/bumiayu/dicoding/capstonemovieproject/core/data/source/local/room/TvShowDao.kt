package com.bumiayu.dicoding.capstonemovieproject.core.data.source.local.room

import androidx.paging.PagingSource
import androidx.room.*
import androidx.sqlite.db.SimpleSQLiteQuery
import com.bumiayu.dicoding.capstonemovieproject.core.data.source.local.entities.TvShowDetailEntity
import com.bumiayu.dicoding.capstonemovieproject.core.data.source.local.entities.TvShowEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TvShowDao {
    @RawQuery(observedEntities = [TvShowEntity::class])
    fun getTvShows(query: SimpleSQLiteQuery): PagingSource<Int, TvShowEntity>

    @Query("SELECT * FROM tb_detail_tvshow WHERE isFavorite = 1")
    fun getListFavoriteTvShows(): PagingSource<Int, TvShowDetailEntity>

    @Query("SELECT * FROM tb_list_tvshow WHERE title LIKE :query ORDER BY title")
    fun getListSearchTvShows(query: String): PagingSource<Int, TvShowEntity>

    @Query("SELECT * FROM tb_detail_tvshow WHERE id = :tvShowId")
    fun getDetailTvShowById(tvShowId: Int): Flow<TvShowDetailEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE, entity = TvShowEntity::class)
    suspend fun insertTvShows(tvShows: List<TvShowEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE, entity = TvShowDetailEntity::class)
    suspend fun insertDetailTvShow(tvShow: TvShowDetailEntity)

    @Update(entity = TvShowDetailEntity::class)
    fun updateDetailTvShow(tvShow: TvShowDetailEntity)
}