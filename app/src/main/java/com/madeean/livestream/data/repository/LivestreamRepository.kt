package com.madeean.livestream.data.repository

import com.madeean.livestream.domain.entity.LivestreamKeysData
import com.madeean.livestream.domain.entity.LivestreamStatistic

interface LivestreamRepository {
    suspend fun getLivestreamData(): List<LivestreamKeysData>
    suspend fun getLivestreamViewCount(): Int
    suspend fun postViewCount(liveStat: LivestreamStatistic)
}