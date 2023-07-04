package com.madeean.livestream.domain

import com.madeean.livestream.domain.entity.LivestreamData

class LivestreamUsecaseImpl: LivestreamUsecase {
    private lateinit var liveStreamRepository: LivestreamRepositoryImpl
    override fun getLivestreamData(): LivestreamData {
        return liveStreamRepository.getLiveStreamData()
    }
}