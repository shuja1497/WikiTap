package com.shuja1497.wikitap.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.shuja1497.wikitap.utilities.DATABASE_NAME

@Database(entities = [Page::class, PageQuery::class], version = 1)
abstract class PageDatabase : RoomDatabase() {

    abstract fun pageDao(): PageDao
    abstract fun pageQueryDao(): PageQueryDao

    companion object {

        @Volatile
        private var instance: PageDatabase? = null
        private var LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            PageDatabase::class.java,
            DATABASE_NAME
        ).build()
    }
}