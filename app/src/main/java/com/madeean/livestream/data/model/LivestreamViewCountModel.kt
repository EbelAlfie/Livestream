package com.madeean.livestream.data.model

import com.google.gson.annotations.SerializedName

data class LivestreamViewCountModel (
    @SerializedName("view_count")
    val viewCount: Int = 0
    ) {
    companion object {
        fun transform(viewCount: LivestreamViewCountModel): Int {
            return viewCount.viewCount
        }
    }
}