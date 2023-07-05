package com.madeean.livestream.domain

import com.madeean.livestream.data.repository.LivestreamRepositoryImpl
import com.madeean.livestream.domain.entity.LivestreamKeysData
import com.madeean.livestream.domain.entity.LivestreamStatistic

class LivestreamUsecaseImpl (private val liveStreamRepository: LivestreamRepositoryImpl): LivestreamUsecase {
    override suspend fun getLivestreamData(): List<LivestreamKeysData> {
        return liveStreamRepository.getLivestreamData()
    }
    override suspend fun getLivestreamViewCount(streamKey: String): Int {
        return liveStreamRepository.getLivestreamViewCount(streamKey)
    }

    override suspend fun postViewCount(data: LivestreamStatistic) {
        return liveStreamRepository.postViewCount(data)
    }
}