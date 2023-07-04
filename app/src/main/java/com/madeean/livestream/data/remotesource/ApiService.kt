package com.madeean.livestream.data.remotesource

import com.madeean.livestream.data.model.LivestreamDataModel
import com.madeean.livestream.domain.entity.LivestreamData
import retrofit2.http.GET
import java.util.concurrent.Flow

interface ApiService {
    @GET("/")
    fun getLiveStreamData(): List<LivestreamDataModel>
}