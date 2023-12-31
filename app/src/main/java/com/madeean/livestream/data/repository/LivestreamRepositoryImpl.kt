package com.madeean.livestream.data.repository

import android.util.Log
import com.madeean.livestream.data.model.LikeDataModel
import com.madeean.livestream.data.model.LivestreamDataModel
import com.madeean.livestream.data.model.LivestreamStatisticModel
import com.madeean.livestream.data.model.LivestreamViewCountModel
import com.madeean.livestream.data.remotesource.RetrofitObj
import com.madeean.livestream.domain.entity.LikeDomainModel
import com.madeean.livestream.domain.entity.LivestreamData

class LivestreamRepositoryImpl: LivestreamRepository {
    override suspend fun getLivestreamData(): List<LivestreamData> {
        return try {
            val data = RetrofitObj.apiService().getLiveStreamData()
            LivestreamDataModel.transform(data)
        } catch(e: Exception) {
            Log.d("test", e.toString())
            listOf()
        }
    }

    override suspend fun getLivestreamViewCount(streamKey: String): Int {
        return try {
            val data = RetrofitObj.apiService().getLiveStreamView(streamKey)
            LivestreamViewCountModel.transform(data)
        } catch (e: Exception) {
            Log.d("error", e.toString())
            0
        }
    }

    override suspend fun postViewCount(liveStat: LivestreamStatisticModel) {
        try {
            RetrofitObj.apiService().postViewCount(liveStat)
        } catch (e: Exception) {
            Log.d("postview", e.toString())
        }
    }

    override suspend fun addLike(streamKey: String): LikeDomainModel {
        return try{
            val data = RetrofitObj.apiService().addLike(streamKey)
            LikeDataModel.transform(data)
        }catch (e:java.lang.Exception){
            e.printStackTrace()
            LikeDomainModel(0)
        }
    }


}