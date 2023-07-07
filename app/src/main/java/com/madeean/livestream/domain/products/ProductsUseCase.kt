package com.madeean.livestream.domain.products

import android.graphics.ColorSpace.Model
import com.madeean.livestream.domain.products.model.ModelProductListDomain

interface ProductsUseCase {
  suspend fun getActiveProduct():ArrayList<ModelProductListDomain>
}