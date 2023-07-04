package com.madeean.livestream.data.repository

import com.madeean.livestream.data.model.LivestreamDataModel
import com.madeean.livestream.data.remotesource.RetrofitObj
import com.madeean.livestream.domain.entity.LivestreamData

class LivestreamRepositoryImpl: LivestreamRepository {
    override fun getLivestreamData(): List<LivestreamData> {
        val data = RetrofitObj.apiService().getLiveStreamData()
        return LivestreamDataModel.transform(data)
    }

}