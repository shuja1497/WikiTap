package com.shuja1497.wikitap.views

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.shuja1497.wikitap.R
import com.shuja1497.wikitap.viewmodels.ListViewModel
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: ListViewModel
    private val searchAdapter = SearchAdapter(arrayListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpViewModel()
        initViews()

        searchText.setOnEditorActionListener { view, actionId, _ ->

            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                viewModel.fetchResponse(searchText.text.toString().trim())
                hideKeyBoard(view)
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }


    }

    private fun initViews() {

//        showKeyBoard()
        searchResults.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = searchAdapter
        }

        observeViewModel()
    }

    private fun showKeyBoard() {
//        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,InputMethodManager.HIDE_IMPLICIT_ONLY)

//        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//        imm.showSoftInput(searchText, InputMethodManager.SHOW_FORCED)

        if (searchText.requestFocus()) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(searchText, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    private fun hideKeyBoard(view: TextView) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun observeViewModel() {

        viewModel.searchResponse.observe(this, Observer {

            it?.let {
                searchResults.visibility = View.VISIBLE
                searchAdapter.updateSearchList(it)
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

                if (it) {
                    error.visibility = View.GONE
                    searchResults.visibility = View.GONE
                }
            }
        })
    }

    private fun setUpViewModel() {
        viewModel =
            ViewModelProviders.of(this).get(ListViewModel::class.java) // instantiating a view model
        viewModel.fetchResponse(searchText.text.toString().trim())
    }
}
