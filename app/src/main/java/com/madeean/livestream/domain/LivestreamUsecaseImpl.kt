package com.madeean.livestream.domain

import com.madeean.livestream.data.repository.LivestreamRepositoryImpl
import com.madeean.livestream.domain.entity.LikeDomainModel
import com.madeean.livestream.domain.entity.LivestreamData
import com.madeean.livestream.domain.entity.LivestreamStatistic

class LivestreamUsecaseImpl (private val liveStreamRepository: LivestreamRepositoryImpl): LivestreamUsecase {
    override suspend fun getLivestreamData(): List<LivestreamData> {
        return liveStreamRepository.getLivestreamData()
    }
    override suspend fun getLivestreamViewCount(streamKey: String): Int {
        return liveStreamRepository.getLivestreamViewCount(streamKey)
    }

    override suspend fun postViewCount(data: LivestreamStatistic) {
        return liveStreamRepository.postViewCount(LivestreamStatistic.transform(data))
    }

    override suspend fun addLike(streamKey: String): LikeDomainModel {
        return liveStreamRepository.addLike(streamKey)
    }
}