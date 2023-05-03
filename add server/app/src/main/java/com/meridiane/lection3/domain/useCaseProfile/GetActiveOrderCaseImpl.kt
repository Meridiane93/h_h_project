package com.meridiane.lection3.domain.useCaseProfile

import androidx.paging.PagingData
import com.meridiane.lection3.domain.entity.Order
import com.meridiane.lection3.domain.repository.InterfaceGetActiveOrdersRepository
import kotlinx.coroutines.flow.Flow

interface GetActiveOrderInterface {
    suspend fun getActiveOrder(): List<Order>

    fun getListOrder(): Flow<PagingData<Order>>

    suspend fun getAllOrder(): List<Order>

    fun getListActiveOrder(): Flow<PagingData<Order>>

    suspend fun getSizeAllOrder(): Int
    suspend fun getSizeActiveOrder(): Int
}

class GetActiveOrderImpl(private val interfaceGetActiveOrdersRepository: InterfaceGetActiveOrdersRepository) :
    GetActiveOrderInterface {
    override suspend fun getActiveOrder(): List<Order> =
        interfaceGetActiveOrdersRepository.getActiveOrder()

    override fun getListOrder(): Flow<PagingData<Order>> =
        interfaceGetActiveOrdersRepository.getListOrder()

    override suspend fun getAllOrder(): List<Order> =
        interfaceGetActiveOrdersRepository.getAllOrder()

    override fun getListActiveOrder(): Flow<PagingData<Order>> =
        interfaceGetActiveOrdersRepository.getListActiveOrder()

    override suspend fun getSizeAllOrder(): Int =
        interfaceGetActiveOrdersRepository.getSizeAllOrder()


    override suspend fun getSizeActiveOrder(): Int =
        interfaceGetActiveOrdersRepository.getSizeActiveOrder()

}
