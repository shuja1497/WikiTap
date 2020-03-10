package com.shuja1497.wikitap.viewmodels

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.shuja1497.wikitap.data.remote.SearchRemoteDataSource
import com.shuja1497.wikitap.model.SearchResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class ListViewModel(application: Application) : BaseViewModel(application){

    private val disposable = CompositeDisposable()
    val searchResponse = MutableLiveData<SearchResponse>()
    val loadError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()

    var offset = 0
    var query: String = ""

    fun fetchResponse(searchQuery: String) {

        query = searchQuery

        if (false) {
            fetchFromDatabase()
        } else {
            fetchFromRemote()
        }
    }

    private fun fetchFromRemote() {
        loading.value = true

        disposable.add(
            SearchRemoteDataSource.getSearchResponse(query, offset)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<SearchResponse>() {
                    override fun onSuccess(response: SearchResponse) {
                        storeResponseLocally(response)
                    }

                    override fun onError(e: Throwable) {
                        loading.value = false
                        loadError.value = true
                        e.printStackTrace()
                    }
                })
        )

    }

    private fun storeResponseLocally(response: SearchResponse) {
        loading.value = false
        loadError.value = false
        searchResponse.value = response
    }

    private fun fetchFromDatabase() {
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}