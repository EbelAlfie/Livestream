package com.madeean.livestream.domain

import com.madeean.livestream.data.repository.LivestreamRepositoryImpl
import com.madeean.livestream.domain.entity.LivestreamData

class LivestreamUsecaseImpl: LivestreamUsecase {
    private lateinit var liveStreamRepository: LivestreamRepositoryImpl
    override fun getLivestreamData(): List<LivestreamData> {
        return liveStreamRepository.getLivestreamData()
    }
}