package com.madeean.livestream.data.repository.products

import android.util.Log
import com.madeean.livestream.data.model.ProductsDataModel
import com.madeean.livestream.data.remotesource.RetrofitObj
import com.madeean.livestream.data.repository.LivestreamRepository
import com.madeean.livestream.domain.products.model.ModelProductListDomain

class ProductsRepositoryImpl: ProductsRepository {
  override suspend fun getActiveProduct(): ArrayList<ModelProductListDomain> {
    return try {
      val data = RetrofitObj.apiService().getActiveProduct()
      ProductsDataModel.transforms(data)
    }catch (e:java.lang.Exception){
      Log.d("error",e.toString())
      ProductsDataModel.transforms(arrayListOf())
    }
  }
}