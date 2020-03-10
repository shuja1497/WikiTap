package com.shuja1497.wikitap.model

import com.google.gson.annotations.SerializedName

data class Thumbnail(

    @SerializedName("source")
    val source: String,

    @SerializedName("width")
    val width: Int,

    @SerializedName("height")
    val height: Int
)