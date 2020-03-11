package com.shuja1497.wikitap.views

import androidx.recyclerview.widget.RecyclerView
import com.shuja1497.wikitap.databinding.SearchItemBinding
import com.shuja1497.wikitap.model.Page

class PageViewHolder(private val view: SearchItemBinding) : RecyclerView.ViewHolder(view.root) {

    fun setUI(
        page: Page,
        searchItemClickListener: SearchItemClickListener
    ) {
        view.page = page
        view.listener = searchItemClickListener
    }
}