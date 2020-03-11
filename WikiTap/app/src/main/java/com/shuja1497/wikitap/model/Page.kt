package com.shuja1497.wikitap.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.shuja1497.wikitap.utilities.isValidString

@Entity
data class Page(

    @SerializedName("pageid")
    var pageId: Int,

    @SerializedName("ns")
    var ns: Int,

    @SerializedName("title")
    var title: String,

    @SerializedName("index")
    var index: Int,

    @SerializedName("thumbnail")
    @Ignore
    var thumbnail: Thumbnail?,

    @SerializedName("pageimage")
    var pageImage: String?,

    @SerializedName("pageprops")
    @Ignore
    var pageProperties: PageProperties?,

    @SerializedName("terms")
    @Ignore
    var terms: Terms?,

    @SerializedName("pageviews")
    @Ignore
    var pageViews: HashMap<String, Int>?
) {

    constructor() : this(0, 0, "", 0, null, "",
        null, null, null)

    @PrimaryKey(autoGenerate = true)
    var uuid: Int = 0

    fun getDescription(): String? {

        if (this.pageProperties != null && isValidString(pageProperties!!.shortDescription)) {
            return pageProperties!!.shortDescription
        }

        if (terms != null && terms!!.description != null && terms!!.description!!.isNotEmpty()) {
            return terms!!.description!![0]
        }
        return null
    }
}