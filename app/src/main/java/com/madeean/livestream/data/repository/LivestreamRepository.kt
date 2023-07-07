package com.madeean.livestream.data.repository

import com.madeean.livestream.data.model.LivestreamStatisticModel
import com.madeean.livestream.domain.entity.LivestreamData
import com.madeean.livestream.domain.entity.LivestreamStatistic

interface LivestreamRepository {
    suspend fun getLivestreamData(): List<LivestreamData>
    suspend fun getLivestreamViewCount(streamKey: String): Int
    suspend fun postViewCount(liveStat: LivestreamStatisticModel)
}