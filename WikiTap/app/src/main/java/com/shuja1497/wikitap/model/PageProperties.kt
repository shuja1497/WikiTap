package com.shuja1497.wikitap.model

import com.google.gson.annotations.SerializedName

data class PageProperties(

    @SerializedName("defaultsort")
    val defaultSort: String?,

    @SerializedName("page_image_free")
    val pageImageFree: String?,

    @SerializedName("wikibase-shortdesc")
    val shortDescription: String?
)