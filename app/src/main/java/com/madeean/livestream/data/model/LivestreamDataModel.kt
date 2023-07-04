package com.madeean.livestream.data.model

import com.google.gson.annotations.SerializedName
import com.madeean.livestream.domain.entity.LivestreamKeysData

data class LivestreamDataModel (
    @SerializedName("stream_key")
    val streamKey: String?,
    @SerializedName("view_count")
    val viewCount: Int = 0
) {
    companion object {
        fun transform(livestreamList: List<LivestreamDataModel>): List<LivestreamKeysData> {
            return livestreamList.map {
                LivestreamKeysData(it.streamKey ?: "")
            }
        }
    }
}