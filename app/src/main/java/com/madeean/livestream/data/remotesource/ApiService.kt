package com.madeean.livestream.data.remotesource

import com.madeean.livestream.data.model.LivestreamDataModel
import retrofit2.http.GET

interface ApiService {
    @GET("obs/")
    suspend fun getLiveStreamData(): List<LivestreamDataModel>
}