package com.madeean.livestream.domain.entity

import com.madeean.livestream.data.model.LivestreamStatisticModel

data class LivestreamStatistic (
    val streamKey: String,
    val isViewing: Boolean
    ) {
    companion object {
        fun transform(it: LivestreamStatistic): LivestreamStatisticModel {
            return LivestreamStatisticModel(it.streamKey, it.isViewing)
        }
    }
}