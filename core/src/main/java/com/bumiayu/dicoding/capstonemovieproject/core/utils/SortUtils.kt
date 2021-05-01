package com.bumiayu.dicoding.capstonemovieproject.core.utils

import androidx.sqlite.db.SimpleSQLiteQuery

object SortUtils {
    const val TITLE = "Title"
    const val VOTE_BEST = "Best"
    const val VOTE_WORST = "Worst"
    const val RANDOM = "Random"
    const val MOVIE_ENTITIES = "tb_list_movies"
    const val TV_SHOW_ENTITIES = "tb_list_tvshow"

    fun getSortedQuery(filter: String, table_name: String): SimpleSQLiteQuery {
        val simpleQuery = StringBuilder().append("SELECT * FROM $table_name ")
        when (filter) {
            TITLE -> simpleQuery.append("ORDER BY title ASC")
            VOTE_BEST -> simpleQuery.append("ORDER BY score DESC")
            VOTE_WORST -> simpleQuery.append("ORDER BY score ASC")
            RANDOM -> simpleQuery.append("ORDER BY RANDOM()")
        }
        return SimpleSQLiteQuery(simpleQuery.toString())
    }
}