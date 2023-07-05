package com.madeean.livestream.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.madeean.livestream.data.repository.LivestreamRepositoryImpl
import com.madeean.livestream.domain.LivestreamUsecaseImpl
import com.madeean.livestream.domain.entity.LivestreamKeysData
import com.madeean.livestream.domain.entity.LivestreamStatistic
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class LivestreamViewModel() : ViewModel(){
    private val usecaseImpl: LivestreamUsecaseImpl

    private val _livestreamViewCount: MutableLiveData<Int> = MutableLiveData()
    fun getLivestreamViewCount(): LiveData<Int> = _livestreamViewCount

    private val _livestreamkeys: MutableLiveData<List<LivestreamKeysData>> = MutableLiveData()
    fun getLivestreamData(): LiveData<List<LivestreamKeysData>> = _livestreamkeys

    init {
        usecaseImpl = LivestreamUsecaseImpl(LivestreamRepositoryImpl())
        fetchLiveStreams()
    }

    private fun fetchLiveStreams() {
        CoroutineScope(IO).launch {
            _livestreamkeys.postValue(usecaseImpl.getLivestreamData())
        }
    }

    fun getLiveViewCount(streamKey: String) {
        CoroutineScope(IO).launch {
            _livestreamViewCount.postValue(usecaseImpl.getLivestreamViewCount(streamKey))
        }
    }

    fun postViewCount(streamKey: String, isViewing: Boolean) {
        CoroutineScope(IO).launch {
            usecaseImpl.postViewCount(
                LivestreamStatistic(streamKey= streamKey, isViewing = isViewing)
            )
        }
    }

}