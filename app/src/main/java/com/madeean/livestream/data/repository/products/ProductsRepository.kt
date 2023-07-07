package com.madeean.livestream.data.repository.products

import com.madeean.livestream.domain.products.model.ModelProductListDomain

interface ProductsRepository {
  suspend fun getActiveProduct():ArrayList<ModelProductListDomain>

}