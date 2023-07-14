package com.madeean.livestream.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.media3.common.StreamKey
import com.madeean.livestream.data.repository.LivestreamRepositoryImpl
import com.madeean.livestream.domain.LivestreamUsecaseImpl
import com.madeean.livestream.domain.entity.LikeDomainModel
import com.madeean.livestream.domain.entity.LivestreamData
import com.madeean.livestream.domain.entity.LivestreamStatistic
import com.madeean.livestream.domain.products.model.ModelProductListDomain
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class LivestreamViewModel() : ViewModel(){
    private val usecaseImpl: LivestreamUsecaseImpl = LivestreamUsecaseImpl(LivestreamRepositoryImpl())
    private val _livestreamkeys: MutableLiveData<List<LivestreamData>> = MutableLiveData()
    fun getLivestreamData(): LiveData<List<LivestreamData>> = _livestreamkeys

    init {
        fetchLiveStreams()
    }

    private fun fetchLiveStreams() {
        CoroutineScope(IO).launch {
            _livestreamkeys.postValue(usecaseImpl.getLivestreamData())
        }
    }


}