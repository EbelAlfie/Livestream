package com.madeean.livestream.domain

import com.madeean.livestream.domain.entity.LivestreamKeysData
import com.madeean.livestream.domain.entity.LivestreamStatistic

interface LivestreamUsecase {

    suspend fun getLivestreamData(): List<LivestreamKeysData>
    suspend fun getLivestreamViewCount(streamKey: String): Int

    suspend fun postViewCount(data: LivestreamStatistic)
}