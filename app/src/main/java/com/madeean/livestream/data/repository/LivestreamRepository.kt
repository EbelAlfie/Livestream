package com.madeean.livestream.data.repository

import com.madeean.livestream.domain.entity.LivestreamData

interface LivestreamRepository {
    suspend fun getLivestreamData(): List<LivestreamData>
}