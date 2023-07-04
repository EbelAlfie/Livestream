package com.madeean.livestream.data.repository

import com.madeean.livestream.data.model.LivestreamDataModel
import com.madeean.livestream.data.model.LivestreamViewCountModel
import com.madeean.livestream.data.remotesource.RetrofitObj
import com.madeean.livestream.domain.entity.LivestreamKeysData
import com.madeean.livestream.domain.entity.LivestreamStatistic

class LivestreamRepositoryImpl: LivestreamRepository {
    override suspend fun getLivestreamData(): List<LivestreamKeysData> {
        val data = RetrofitObj.apiService().getLiveStreamData()
        return LivestreamDataModel.transform(data)
    }

    override suspend fun getLivestreamViewCount(streamKey: String): Int {
        val data = RetrofitObj.apiService().getLiveStreamView(streamKey)
        return LivestreamViewCountModel.transform(data)
    }

    override suspend fun postViewCount(liveStat: LivestreamStatistic) {
        val data = RetrofitObj.apiService().postViewCount()
    }

}