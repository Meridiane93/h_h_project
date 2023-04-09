package com.meridiane.lection3.domain.useCaseProducts

import com.meridiane.lection3.domain.models.Product
import com.meridiane.lection3.domain.repository.InterfaceGetProductDetailsRepository
import kotlinx.coroutines.flow.Flow

interface GetProductDetailsInterface {
    suspend fun product(id: String): Flow<Product>
}

class GetProductDetailsUseCaseImpl(
    private val interfaceGetProductDetailsRepository: InterfaceGetProductDetailsRepository
) : GetProductDetailsInterface {

    override suspend fun product(id: String): Flow<Product> =
        interfaceGetProductDetailsRepository.getDetailsProduct(id)

}