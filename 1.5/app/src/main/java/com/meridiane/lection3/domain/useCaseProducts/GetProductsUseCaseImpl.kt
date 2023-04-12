package com.meridiane.lection3.domain.useCaseProducts

import androidx.paging.PagingData
import com.meridiane.lection3.domain.models.Product
import com.meridiane.lection3.domain.repository.InterfaceGetProductsRepository
import kotlinx.coroutines.flow.Flow

interface GetProductsInterface {
    suspend fun product(): Flow<PagingData<Product>>
}

class GetProductsUseCaseImpl(
    private val interfaceGetProductsRepository: InterfaceGetProductsRepository
) : GetProductsInterface {

    //override suspend fun authorization(): List<Product> =
    //    interfaceGetProductsRepository.getProducts2()

    override suspend fun product(): Flow<PagingData<Product>> =
        interfaceGetProductsRepository.getList()

}