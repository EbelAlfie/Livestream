package com.madeean.livestream.data.model

import com.google.gson.annotations.SerializedName
import com.madeean.livestream.domain.entity.LivestreamData

data class LivestreamDataModel (
    @SerializedName("stream_key")
    val streamKey: String?,
    @SerializedName("view_count")
    val viewCount: Int = 0,
    @SerializedName("preview_image")
    val previewImage: String?
) {
    companion object {
        fun transform(livestreamList: List<LivestreamDataModel>): List<LivestreamData> {
            return livestreamList.map {
                LivestreamData(it.streamKey ?: "", it.previewImage ?: "")
            }
        }
    }
}