package com.shuja1497.wikitap.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PageDao {

    @Insert
    suspend fun insertAllPages(vararg page: Page): List<Long>

    @Query("select * from page")
    suspend fun getAllPages(): List<Page>

    @Query("delete from page")
    suspend fun deleteAllPages()

}