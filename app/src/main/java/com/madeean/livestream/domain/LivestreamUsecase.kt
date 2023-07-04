package com.madeean.livestream.domain

import com.madeean.livestream.domain.entity.LivestreamData

interface LivestreamUsecase {

    suspend fun getLivestreamData(): List<LivestreamData>
}