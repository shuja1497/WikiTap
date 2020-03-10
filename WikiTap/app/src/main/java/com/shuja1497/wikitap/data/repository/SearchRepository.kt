package com.shuja1497.wikitap.data.repository

import com.shuja1497.wikitap.data.remote.SearchRemoteDataSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

object SearchRepository {

    fun getSearchResponse(query: String, offset: Int) {

        SearchRemoteDataSource.getSearchResponse(query, offset)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableSingleObserver<Void>() {
                override fun onSuccess(value: Void) {
                }

                override fun onError(e: Throwable) {
                }
            })
    }
}