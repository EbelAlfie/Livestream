package com.madeean.livestream.domain

import com.madeean.livestream.data.repository.LivestreamRepositoryImpl
import com.madeean.livestream.domain.entity.LivestreamData
import kotlinx.coroutines.flow.Flow

class LivestreamUsecaseImpl: LivestreamUsecase {
    private val liveStreamRepository: LivestreamRepositoryImpl = LivestreamRepositoryImpl()
    override suspend fun getLivestreamData(): List<LivestreamData> {
        return liveStreamRepository.getLivestreamData()
    }
}