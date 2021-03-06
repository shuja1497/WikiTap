package com.shuja1497.wikitap.views

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.shuja1497.wikitap.R
import com.shuja1497.wikitap.databinding.LoadingItemBinding
import com.shuja1497.wikitap.databinding.SearchItemBinding
import com.shuja1497.wikitap.model.Page
import com.shuja1497.wikitap.model.SearchResponse
import com.shuja1497.wikitap.utilities.BASE_WEB_VIEW_URL
import com.shuja1497.wikitap.utilities.INTENT_EXTRAS_URL
import com.shuja1497.wikitap.utilities.VIEW_TYPE_LOADER
import com.shuja1497.wikitap.utilities.VIEW_TYPE_PAGE
import kotlinx.android.synthetic.main.search_item.view.*

class SearchAdapter(private val pages: ArrayList<Page?>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(), SearchItemClickListener {

    fun updateSearchList(searchResponse: SearchResponse) {

        if (searchResponse.query?.pages != null) {
            pages.clear()
            pages.addAll(searchResponse.query.pages!!.sortedBy { it.index })
            notifyDataSetChanged()
        }
    }

    fun appendSearchList(searchResponse: SearchResponse) {
        val oldPosition = pages.size
        if (searchResponse.query?.pages != null) {
            pages.addAll(searchResponse.query.pages!!.sortedBy { it.index })
            notifyItemRangeChanged(oldPosition, pages.size-1)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return when (viewType) {

            VIEW_TYPE_PAGE -> {
                val view = DataBindingUtil.inflate<SearchItemBinding>(
                    inflater,
                    R.layout.search_item,
                    parent,
                    false
                )
                PageViewHolder(view)
            }

            else -> {
                val view = DataBindingUtil.inflate<LoadingItemBinding>(
                    inflater,
                    R.layout.loading_item,
                    parent,
                    false
                )
                LoadingViewHolder(view)
            }
        }
    }

    override fun getItemCount(): Int = pages.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (holder) {
            is PageViewHolder -> {
                holder.setUI(pages[position]!!, this)
            }

            is LoadingViewHolder -> {
                holder.setUI()
            }
        }
    }

    override fun getItemViewType(position: Int): Int {

        if (pages[position] == null) {
            return VIEW_TYPE_LOADER
        }

        return VIEW_TYPE_PAGE
    }

    override fun onSearchItemClicked(view: View) {
        val url = BASE_WEB_VIEW_URL + view.title.text.toString().replace(" ", "_")

        val intent = Intent(view.context, WebViewActivity::class.java).apply {
            putExtra(INTENT_EXTRAS_URL, url)
        }

        view.context.startActivity(intent)
    }

    fun addFooter() {
        if (itemCount > 0) {
            pages.add(null)
            notifyItemInserted(pages.size)
        }
    }

    fun removeFooter() {
        if (itemCount > 1 && pages[itemCount-1] == null) {
            pages.removeAt(pages.size - 1)
            notifyItemRemoved(pages.size)
        }
    }

    fun clearList() {
        pages.clear()
        notifyDataSetChanged()
    }
}