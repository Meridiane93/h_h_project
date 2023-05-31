package com.meridiane.lection3.domain.useCaseProducts

import com.meridiane.lection3.domain.entity.order.AddOrder
import com.meridiane.lection3.domain.entity.product_detail.ProductDetail
import com.meridiane.lection3.domain.repository.products.InterfaceGetProductDetailsRepository
import kotlinx.coroutines.flow.Flow

interface GetProductDetailInterface {
    suspend fun getDetailProduct(id: String): Flow<ProductDetail>
    suspend fun addOrder(order: AddOrder): Result<String>
}

class GetProductDetailsUseCaseImpl(
    private val interfaceGetProductDetailsRepository: InterfaceGetProductDetailsRepository
) : GetProductDetailInterface {

    override suspend fun getDetailProduct(id: String): Flow<ProductDetail> =
        interfaceGetProductDetailsRepository.getDetailsProduct(id)

    override suspend fun addOrder(order: AddOrder): Result<String> {

        val answer = interfaceGetProductDetailsRepository.addOrder(order).status
        return try {
            Result.success(answer)
        }
        catch (e:Exception){
            Result.failure(e)
        }
    }

}