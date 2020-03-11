package com.shuja1497.wikitap.views

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shuja1497.wikitap.R
import com.shuja1497.wikitap.databinding.RecentSearchItemBinding
import com.shuja1497.wikitap.utilities.isInValidString
import com.shuja1497.wikitap.viewmodels.ListViewModel
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private var pagination: Boolean = true
    private lateinit var viewModel: ListViewModel
    private val searchAdapter = SearchAdapter(arrayListOf())
    private var query = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpViewModel()
        initViews()

        searchText.setOnEditorActionListener { view, actionId, _ ->

            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                query = searchText.text.toString().trim()

                if (isInValidString(query)) {
                    Toast.makeText(
                        this,
                        getString(R.string.valid_query_message),
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnEditorActionListener false
                }

                searchAdapter.clearList()
                viewModel.fetchResponse(query, false)
                hideKeyBoard(view)
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
    }

    private fun initViews() {

        val linearLayoutManager = LinearLayoutManager(this)
        searchResults.apply {
            layoutManager = linearLayoutManager
            adapter = searchAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    if (linearLayoutManager.findLastVisibleItemPosition() >=
                        (searchAdapter.itemCount - 2) && pagination
                    ) {
                        viewModel.loading.value?.let {
                            if (!it) {
                                searchAdapter.addFooter()
                                viewModel.fetchResponse(query, true)
                            }
                        }
                    }
                }
            })
        }

        observeViewModel()
    }

    private fun hideKeyBoard(view: TextView) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun observeViewModel() {

        viewModel.searchResponse.observe(this, Observer {

            it?.let {
                searchResults.visibility = View.VISIBLE

                if (searchAdapter.itemCount > 0) {
                    searchAdapter.removeFooter()
                    searchAdapter.appendSearchList(it)
                } else {
                    searchAdapter.updateSearchList(it)
                }
            }
        })

        viewModel.loadError.observe(this, Observer {

            it?.let {
                error.visibility = if (it) View.VISIBLE else View.GONE
            }
        })

        viewModel.loading.observe(this, Observer { isLoading ->
            isLoading?.let {
                searchProgressBar.visibility = if (it) View.VISIBLE else View.GONE

                if (it && searchAdapter.itemCount == 0) {
                    error.visibility = View.GONE
                    searchResults.visibility = View.GONE
                }

                if (searchAdapter.itemCount > 0) {
                    searchProgressBar.visibility = View.GONE
                }
            }
        })

        viewModel.dbResponse.observe(this, Observer {

            it?.let { queries ->
                offlineLinearLayout.removeAllViews()

                if (queries.isNotEmpty()) {
                    recentSearchTitle.visibility = View.VISIBLE
                }

                for (pageQuery in queries.distinct().asReversed()) {

                    if (offlineLinearLayout.childCount == 5) {
                        break
                    }

                    val view = DataBindingUtil.inflate<RecentSearchItemBinding>(
                        layoutInflater,
                        R.layout.recent_search_item,
                        offlineLinearLayout,
                        false
                    )

                    view.pageQuery = pageQuery
                    offlineLinearLayout.addView(view.root)
                    view.root.setOnClickListener {
                        searchAdapter.clearList()
                        query = pageQuery.query
                        viewModel.fetchResponse(pageQuery.query, false)
                        hideKeyBoard(searchText)
                    }
                }
                offlineLinearLayout.requestFocus()
            }
        })

        viewModel.isBatchComplete.observe(this, Observer {

            it?.let {

                pagination = !it
                if (it) {
                    searchAdapter.removeFooter()
                }
            }
        })
    }

    private fun setUpViewModel() {
        viewModel =
            ViewModelProviders.of(this).get(ListViewModel::class.java) // instantiating a view model
        viewModel.fetchFromDatabase()
    }
}
