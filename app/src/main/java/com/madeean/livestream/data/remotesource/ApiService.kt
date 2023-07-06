package com.madeean.livestream.data.remotesource

import com.madeean.livestream.data.model.LivestreamDataModel
import com.madeean.livestream.data.model.LivestreamStatisticModel
import com.madeean.livestream.data.model.LivestreamViewCountModel
import retrofit2.http.*

interface ApiService {
    @GET("obs/")
    suspend fun getLiveStreamData(): List<LivestreamDataModel>
    @GET("obs/view-count")
    suspend fun getLiveStreamView(
        @Query("stream_key") streamKey: String
    ): LivestreamViewCountModel
    @Headers("content-type: application/json")
    @POST("obs/view")
    suspend fun postViewCount(
        @Body liveStat: LivestreamStatisticModel
    )
}