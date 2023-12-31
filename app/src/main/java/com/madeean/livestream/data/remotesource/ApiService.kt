package com.madeean.livestream.data.remotesource

import com.madeean.livestream.data.model.LikeDataModel
import com.madeean.livestream.data.model.LivestreamDataModel
import com.madeean.livestream.data.model.LivestreamStatisticModel
import com.madeean.livestream.data.model.LivestreamViewCountModel
import com.madeean.livestream.data.model.ProductsDataModel
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

    @Headers("content-type: application/json")
    @POST("obs/add-like/{streamkey}")
    suspend fun addLike(
        @Path("streamkey") streamkey:String,
    ):LikeDataModel

    @GET("product/active")
    suspend fun getActiveProduct():ArrayList<ProductsDataModel>
}