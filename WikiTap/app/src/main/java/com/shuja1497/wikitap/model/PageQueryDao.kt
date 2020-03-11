package com.shuja1497.wikitap.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PageQueryDao {

    @Insert
    suspend fun insertQuery(query: PageQuery)

    @Query("select * from pagequery")
    suspend fun getAllQueries(): List<PageQuery>

    @Query("delete from pagequery")
    suspend fun deleteAllQueries()

}