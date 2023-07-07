package com.madeean.livestream.data.model

import com.google.gson.annotations.SerializedName
import com.madeean.livestream.domain.products.model.ModelProductListDomain

data class ProductsDataModel(
 val id:Int?,
 val image:String?,
 val name: String?,
 @SerializedName("product_price") val productPrice: Int?,
 @SerializedName("product_spesial_price") val productSpesialPrice: Int?,
 @SerializedName("product_discount") val productDiscount: Int?,
 @SerializedName("is_highlight") val isHighlight:Int?,
 @SerializedName("is_active") val isActive:Int?
) {
  companion object {
    fun transforms(data: ArrayList<ProductsDataModel>):ArrayList<ModelProductListDomain> {
      val list = ArrayList<ModelProductListDomain>()
      data.forEach {
        list.add(transform(it))
      }
      return list
    }

    private fun transform(data:ProductsDataModel):ModelProductListDomain{
      return ModelProductListDomain(
        id = data.id ?:0,
        image = data.image ?: "",
        name = data.name ?: "",
        productPrice = data.productPrice ?: 0,
        productSpesialPrice =  data.productSpesialPrice ?: 0,
        productDiscount = data.productDiscount ?:0,
        isActive = data.isActive ?:-1,
        isHighlight = data.isHighlight?:-1
      )
    }
  }
}
