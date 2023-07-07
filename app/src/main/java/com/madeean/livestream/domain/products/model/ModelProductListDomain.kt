package com.madeean.livestream.domain.products.model

import com.google.gson.annotations.SerializedName


data class ModelProductListDomain(
  val id: Int,
  val image: String,
  val name: String,
  val productPrice: Int,
  val productSpesialPrice: Int,
  val productDiscount: Int,
  val isHighlight: Int,
  val isActive: Int
)