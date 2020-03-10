package com.shuja1497.wikitap.data.remote

import com.shuja1497.wikitap.data.ApiClient
import com.shuja1497.wikitap.utilities.*
import io.reactivex.Single

object SearchRemoteDataSource {

    private var service: SearchService? = null

    @Synchronized
    private fun getService(): SearchService {
        if (service == null) {
            service = ApiClient.getService(SearchService::class.java)
        }
        return service!!
    }

    fun getSearchResponse(query: String, offset: Int): Single<Void> {
        return getService().getSearchResponse(
            query,
            offset,
            API_QUERY_PARAMS_PROP,
            API_QUERY_PARAMS_ACTION,
            API_QUERY_PARAMS_FORMAT,
            API_QUERY_PARAMS_GENERATOR,
            API_QUERY_PARAMS_FORMAT_VERSION,
            API_QUERY_PARAMS_LIMIT
        )
    }

}