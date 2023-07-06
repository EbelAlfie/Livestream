package com.madeean.livestream.data.model

import com.google.gson.annotations.SerializedName

data class LivestreamStatisticModel (
    @SerializedName("stream_key")
    val streamKey: String,
    @SerializedName("is_viewing")
    val isViewing: Boolean
    )