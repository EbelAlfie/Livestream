package com.madeean.livestream.data.remotesource

import com.madeean.livestream.data.model.LivestreamDataModel
import com.madeean.livestream.data.model.LivestreamViewCountModel
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @GET("obs/")
    suspend fun getLiveStreamData(): List<LivestreamDataModel>
    @GET("obs/view-count")
    suspend fun getLiveStreamView(
        @Query("stream_key") streamKey: String
    ): LivestreamViewCountModel
    @POST("obs/view")
    suspend fun postViewCount()
}