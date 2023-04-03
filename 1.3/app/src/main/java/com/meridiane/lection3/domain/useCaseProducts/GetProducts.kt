package com.meridiane.lection3.domain.useCaseProducts

import androidx.paging.PagingData
import com.meridiane.lection3.domain.models.Product
import com.meridiane.lection3.domain.repository.InterfaceGetProductsRepository
import kotlinx.coroutines.flow.Flow

class GetProducts(private val interfaceGetProductsRepository: InterfaceGetProductsRepository) {

    suspend fun authorization(): List<Product> =
        interfaceGetProductsRepository.getProducts2()

    fun productList(): Flow<PagingData<Product>> =
        interfaceGetProductsRepository.getList()


}