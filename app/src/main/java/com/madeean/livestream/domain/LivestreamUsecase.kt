package com.madeean.livestream.domain

import com.madeean.livestream.domain.entity.LikeDomainModel
import com.madeean.livestream.domain.entity.LivestreamData
import com.madeean.livestream.domain.entity.LivestreamStatistic

interface LivestreamUsecase {

    suspend fun getLivestreamData(): List<LivestreamData>
    suspend fun getLivestreamViewCount(streamKey: String): Int

    suspend fun postViewCount(data: LivestreamStatistic)

    suspend fun addLike(streamKey: String):LikeDomainModel
}