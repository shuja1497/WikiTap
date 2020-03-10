package com.shuja1497.wikitap.data.repository

import com.shuja1497.wikitap.data.remote.SearchRemoteDataSource
import com.shuja1497.wikitap.model.SearchResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

object SearchRepository {

    fun getSearchResponse(query: String, offset: Int) {

        SearchRemoteDataSource.getSearchResponse(query, offset)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableSingleObserver<SearchResponse>() {
                override fun onSuccess(value: SearchResponse) {
                    Timber.d(value.toString())
                }

                override fun onError(e: Throwable) {
                    Timber.d(e.localizedMessage)
                }
            })
    }
}