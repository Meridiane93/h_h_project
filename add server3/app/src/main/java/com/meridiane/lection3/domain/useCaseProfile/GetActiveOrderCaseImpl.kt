package com.meridiane.lection3.domain.useCaseProfile

import androidx.paging.PagingData
import com.meridiane.lection3.domain.entity.AllOrder
import com.meridiane.lection3.domain.repository.InterfaceGetActiveOrdersRepository
import kotlinx.coroutines.flow.Flow

interface GetActiveOrderInterface {
   fun getListOrder(): Flow<PagingData<AllOrder>>
   suspend fun cancelOrder(id: String): Result<AllOrder>
}

class GetActiveOrderImpl(private val interfaceGetActiveOrdersRepository: InterfaceGetActiveOrdersRepository) :
    GetActiveOrderInterface {

    override  fun getListOrder(): Flow<PagingData<AllOrder>> =
    interfaceGetActiveOrdersRepository.getOrders()

    override suspend fun cancelOrder(id: String): Result<AllOrder> {
        return try {
             val answer =  interfaceGetActiveOrdersRepository.cancelOrderInterface(id)
             Result.success(answer)
        }
        catch (e:Exception){
            Result.failure(e)
        }
    }



}
