package com.shuja1497.wikitap.viewmodels

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.shuja1497.wikitap.data.remote.SearchRemoteDataSource
import com.shuja1497.wikitap.model.PageDatabase
import com.shuja1497.wikitap.model.PageQuery
import com.shuja1497.wikitap.model.PageQueryDao
import com.shuja1497.wikitap.model.SearchResponse
import com.shuja1497.wikitap.utilities.API_QUERY_PARAMS_LIMIT
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch

class ListViewModel(application: Application) : BaseViewModel(application) {

    private lateinit var queryDao: PageQueryDao
    private val disposable = CompositeDisposable()
    val searchResponse = MutableLiveData<SearchResponse>()
    val dbResponse = MutableLiveData<List<PageQuery>>()
    val loadError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()
    val isBatchComplete = MutableLiveData<Boolean>()

    private var offset = 0
    var increaseOffset = false
    var query: String = ""

    fun fetchResponse(searchQuery: String, increaseOffset: Boolean) {

        this.increaseOffset = increaseOffset
        query = searchQuery
        queryDao = PageDatabase(getApplication()).pageQueryDao()

        launch {
            queryDao.insertOrUpdateQuery(PageQuery(query))
        }

        fetchFromRemote()
    }

    private fun fetchFromRemote() {
        loading.value = true

        if (increaseOffset) {
            offset += API_QUERY_PARAMS_LIMIT.toInt()
        } else {
            offset = 0
        }

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

        if (response.batchComplete && response.query == null) {
            isBatchComplete.value = true
            pagesRetrieved(null)
            return
        }

        isBatchComplete.value = false

        if (response.query?.pages == null) {
            return
        }

        launch {
            val dao = PageDatabase(getApplication()).pageDao()

            dao.deleteAllPages()
            val pages = response.query.pages
            val result = dao.insertAllPages(*pages!!.toTypedArray())

            var i = 0
            while (i < pages.size) {

                pages[i].uuid = result[i].toInt()
                ++i
            }

            response.query.pages = pages
            pagesRetrieved(response)
        }
    }

    private fun pagesRetrieved(response: SearchResponse?) {
        loading.value = false
        loadError.value = false
        searchResponse.value = response
    }

    fun fetchFromDatabase() {

        launch {
            val queryDao = PageDatabase(getApplication()).pageQueryDao()
            val queryList = queryDao.getAllQueries()

            dbResponse.value = queryList
        }
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}