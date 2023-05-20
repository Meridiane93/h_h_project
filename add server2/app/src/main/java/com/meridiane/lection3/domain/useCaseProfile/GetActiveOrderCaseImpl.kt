package com.meridiane.lection3.domain.useCaseProfile

import android.util.Log
import androidx.paging.PagingData
import com.meridiane.lection3.domain.entity.AllOrder
import com.meridiane.lection3.domain.repository.InterfaceGetActiveOrdersRepository
import kotlinx.coroutines.flow.Flow

interface GetActiveOrderInterface {
   suspend fun getListOrder(): Flow<PagingData<AllOrder>>
   suspend fun cancelOrder(id: String): Result<AllOrder>
}

class GetActiveOrderImpl(private val interfaceGetActiveOrdersRepository: InterfaceGetActiveOrdersRepository) :
    GetActiveOrderInterface {

    override suspend fun getListOrder(): Flow<PagingData<AllOrder>> =
    interfaceGetActiveOrdersRepository.getOrders()

    override suspend fun cancelOrder(id: String): Result<AllOrder> {
       val answer =  interfaceGetActiveOrdersRepository.cancelOrderInterface(id)
        return try {
            Result.success(answer)
        }
        catch (e:Exception){
            Result.failure(e)
        }
    }



}
