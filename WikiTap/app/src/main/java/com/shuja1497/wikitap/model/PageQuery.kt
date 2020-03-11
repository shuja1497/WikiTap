package com.shuja1497.wikitap.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PageQuery(
    @PrimaryKey
    val query: String
)