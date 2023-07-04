package com.madeean.livestream.domain

import com.madeean.livestream.domain.entity.LivestreamData

interface LivestreamUsecase {

    fun getLivestreamData(): LivestreamData
}