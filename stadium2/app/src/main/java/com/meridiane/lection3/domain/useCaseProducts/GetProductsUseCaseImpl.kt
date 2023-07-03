package com.meridiane.lection3.domain.useCaseProducts

import androidx.paging.PagingData
import com.meridiane.lection3.domain.entity.product.Product
import com.meridiane.lection3.domain.repository.products.InterfaceGetProductsRepository
import kotlinx.coroutines.flow.Flow

interface GetProductsInterface {
    suspend fun product(): Flow<PagingData<Product>>
}

class GetProductsUseCaseImpl(private val interfaceGetProductsRepository: InterfaceGetProductsRepository)
    : GetProductsInterface {

    override suspend fun product(): Flow<PagingData<Product>> =
        interfaceGetProductsRepository.getProducts()
}