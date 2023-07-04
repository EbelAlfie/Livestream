package com.madeean.livestream.domain

import com.madeean.livestream.domain.entity.LivestreamKeysData
import com.madeean.livestream.domain.entity.LivestreamStatistic

interface LivestreamUsecase {

    suspend fun getLivestreamData(): List<LivestreamKeysData>
    suspend fun getLivestreamViewCount(): Int

    suspend fun postViewCount(isViewing: LivestreamStatistic)
}