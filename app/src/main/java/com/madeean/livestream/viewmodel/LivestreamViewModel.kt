package com.madeean.livestream.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.madeean.livestream.domain.LivestreamUsecaseImpl
import com.madeean.livestream.domain.entity.LivestreamData

class LivestreamViewModel() : ViewModel(){
    private val usecaseImpl: LivestreamUsecaseImpl = LivestreamUsecaseImpl()
    private val _livestreamData: MutableLiveData<List<LivestreamData>> = MutableLiveData()
    fun getLivestreamData(): LiveData<List<LivestreamData>> = _livestreamData

    init {
        fetchLiveStreams()
    }

    private fun fetchLiveStreams() {
        _livestreamData.value = usecaseImpl.getLivestreamData()
    }

}