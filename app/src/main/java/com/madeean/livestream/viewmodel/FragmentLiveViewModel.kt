package com.madeean.livestream.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.madeean.livestream.data.repository.LivestreamRepositoryImpl
import com.madeean.livestream.domain.LivestreamUsecaseImpl
import com.madeean.livestream.domain.entity.LivestreamStatistic
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FragmentLiveViewModel : ViewModel(){
    private val usecaseImpl: LivestreamUsecaseImpl =
        LivestreamUsecaseImpl(LivestreamRepositoryImpl())

    private val _livestreamViewCount: MutableLiveData<Int> = MutableLiveData()
    fun getLivestreamViewCount(): LiveData<Int> = _livestreamViewCount

    fun getLiveViewCount(streamKey: String) {
        CoroutineScope(Dispatchers.IO).launch {
            _livestreamViewCount.postValue(usecaseImpl.getLivestreamViewCount(streamKey))
        }
    }

    fun postViewCount(streamKey: String, isViewing: Boolean) {
        CoroutineScope(Dispatchers.IO).launch {
            usecaseImpl.postViewCount(
                LivestreamStatistic(streamKey= streamKey, isViewing = isViewing)
            )
        }
    }

}