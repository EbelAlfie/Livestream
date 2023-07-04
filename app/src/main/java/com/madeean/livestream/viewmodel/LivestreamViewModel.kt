package com.madeean.livestream.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.madeean.livestream.domain.LivestreamUsecaseImpl
import com.madeean.livestream.domain.entity.LivestreamData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class LivestreamViewModel() : ViewModel(){
    private val usecaseImpl: LivestreamUsecaseImpl = LivestreamUsecaseImpl()
    private val _livestreamData: MutableLiveData<List<LivestreamData>> = MutableLiveData()
    fun getLivestreamData(): LiveData<List<LivestreamData>> = _livestreamData

    init {
        fetchLiveStreams()
    }

    private fun fetchLiveStreams() {
        CoroutineScope(IO).launch {
            _livestreamData.postValue(usecaseImpl.getLivestreamData())
        }
    }

}