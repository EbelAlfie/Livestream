package com.madeean.livestream.data.repository

import com.madeean.livestream.domain.entity.LivestreamData
import kotlinx.coroutines.flow.Flow

interface LivestreamRepository {
    fun getLivestreamData(): List<LivestreamData>
}