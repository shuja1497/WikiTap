package com.shuja1497.wikitap.model

import com.google.gson.annotations.SerializedName

data class Page(

    @SerializedName("pageid")
    val pageId: Int,

    @SerializedName("ns")
    val ns: Int,

    @SerializedName("title")
    val title: String,

    @SerializedName("index")
    val index: Int,

    @SerializedName("thumbnail")
    val thumbnail: Thumbnail?,

    @SerializedName("pageimage")
    val pageImage: String?,

    @SerializedName("pageprops")
    val pageProperties: PageProperties?,

    @SerializedName("terms")
    val terms: Terms?,

    @SerializedName("pageviews")
    val pageViews: HashMap<String, Int>?

)