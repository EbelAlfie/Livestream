package com.madeean.livestream.data.model

import com.google.gson.annotations.SerializedName

data class LivestreamDataModel (
    @SerializedName("stream_key")
    val streamKey: String,
    @SerializedName("view_count")
    val viewCount: Int
)