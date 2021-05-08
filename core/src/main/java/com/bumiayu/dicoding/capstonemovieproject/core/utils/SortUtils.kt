package com.bumiayu.dicoding.capstonemovieproject.core.utils

import androidx.sqlite.db.SimpleSQLiteQuery

object SortUtils {
    const val TITLE = "Title"
    const val VOTE_BEST = "Best"
    const val VOTE_WORST = "Worst"
    const val RANDOM = "Random"
    const val MOVIES_TABLE = "tb_list_movies"
    const val TV_SHOW_TABLE = "tb_list_tvshow"

    fun getSortedQuery(sortBy: String, table_name: String): SimpleSQLiteQuery {
        val simpleQuery = StringBuilder().append("SELECT * FROM $table_name ")
        when (sortBy) {
            TITLE -> simpleQuery.append("ORDER BY title ASC")
            VOTE_BEST -> simpleQuery.append("ORDER BY score DESC")
            VOTE_WORST -> simpleQuery.append("ORDER BY score ASC")
            RANDOM -> simpleQuery.append("ORDER BY RANDOM()")
        }
        return SimpleSQLiteQuery(simpleQuery.toString())
    }
}