package com.shuja1497.wikitap.model

import com.google.gson.annotations.SerializedName

data class SearchResponse(

    @SerializedName("batchcomplete")
    val batchComplete: Boolean,

    @SerializedName("query")
    val query: Query?
)


data class Query(
    @SerializedName("pages")
    var pages: ArrayList<Page>?
)