package com.madeean.livestream.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.madeean.livestream.data.repository.LivestreamRepositoryImpl
import com.madeean.livestream.data.repository.products.ProductsRepositoryImpl
import com.madeean.livestream.domain.LivestreamUsecaseImpl
import com.madeean.livestream.domain.products.ProductsUseCaseImpl
import com.madeean.livestream.domain.products.model.ModelProductListDomain
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductsViewModel: ViewModel() {
  private val usecaseImpl: ProductsUseCaseImpl = ProductsUseCaseImpl(ProductsRepositoryImpl())
private var _products = MutableLiveData<ArrayList<ModelProductListDomain>>()
  val products: LiveData<ArrayList<ModelProductListDomain>> = _products

  fun getActiveProduct(){
    CoroutineScope(Dispatchers.IO).launch {
      _products.postValue(usecaseImpl.getActiveProduct())
    }
  }



}