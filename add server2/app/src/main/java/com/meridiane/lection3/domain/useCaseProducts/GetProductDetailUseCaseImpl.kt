package com.meridiane.lection3.domain.useCaseProducts

import android.util.Log
import com.meridiane.lection3.domain.entity.AddOrder
import com.meridiane.lection3.domain.entity.product_detail.ProductDetail
import com.meridiane.lection3.domain.repository.InterfaceGetProductDetailsRepository
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
            Log.d("MyTag","Try $answer")
            Result.success(answer)
        }
        catch (e:Exception){
            Log.d("MyTag","Cath $answer")
            Result.failure(e)
        }
    }

}