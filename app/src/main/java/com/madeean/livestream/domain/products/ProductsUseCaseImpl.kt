package com.madeean.livestream.domain.products

import com.madeean.livestream.data.repository.LivestreamRepositoryImpl
import com.madeean.livestream.data.repository.products.ProductsRepositoryImpl
import com.madeean.livestream.domain.LivestreamUsecase
import com.madeean.livestream.domain.products.model.ModelProductListDomain

class ProductsUseCaseImpl (private val productsRepository: ProductsRepositoryImpl):
  ProductsUseCase {
  override suspend fun getActiveProduct(): ArrayList<ModelProductListDomain> {
    return productsRepository.getActiveProduct()
  }
}