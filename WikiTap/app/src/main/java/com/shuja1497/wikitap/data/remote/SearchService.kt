package com.shuja1497.wikitap.data.remote

import com.shuja1497.wikitap.model.SearchResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchService {

    @GET("/w/api.php")
    fun getSearchResponse(
        @Query("gpssearch") srsearch: String,
        @Query("gpsoffset") offset: Int,
        @Query("prop") prop: String,
        @Query("action") action: String,
        @Query("format") format: String,
        @Query("generator") generator: String,
        @Query("formatversion") formatVersion: String,
        @Query("gpslimit") limit: String
    ): Single<SearchResponse>

}