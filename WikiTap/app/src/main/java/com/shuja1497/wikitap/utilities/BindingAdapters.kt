package com.shuja1497.wikitap.utilities

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.shuja1497.wikitap.R
import java.util.*

@BindingAdapter("android:imageUrl")
fun loadImage(view: ImageView, url: String?) {
    view.loadImage(url, getProgressDrawable(view.context))
}

fun getProgressDrawable(context: Context): CircularProgressDrawable {
    return CircularProgressDrawable(context).apply {
        strokeWidth = 5f
        centerRadius = 50f
        start()
    }
}

fun ImageView.loadImage(url: String?, progressDrawable: CircularProgressDrawable) {

    val options = RequestOptions()
        .placeholder(R.drawable.wallpaper_24dp)
        .error(R.drawable.wallpaper_24dp)

    Glide.with(context)
        .setDefaultRequestOptions(options)
        .load(url)
        .into(this)
}

@BindingAdapter("android:setText")
fun setText(view: TextView, text: String?) {

    if (isInValidString(text)) {
        view.visibility = View.GONE
    } else {
        view.visibility = View.VISIBLE
        view.text = text
    }
}

fun isInValidString(text: String?): Boolean {

    return text == null ||
            text.isEmpty() ||
            text.trim() == "" ||
            text.trim().toLowerCase(Locale.ROOT) == "Null".toLowerCase(Locale.ROOT) ||
            text.trim().toLowerCase(Locale.ROOT) == "None".toLowerCase(Locale.ROOT)
}