package com.meridiane.lection3.domain.useCaseProfile

import androidx.paging.PagingData
import com.meridiane.lection3.domain.models.Order
import com.meridiane.lection3.domain.repository.InterfaceGetAllOrdersRepository
import kotlinx.coroutines.flow.Flow

interface GetAllOrderInterface {
    suspend fun getAllOrder(): Flow<PagingData<Order>>
}

class GetAllOrderImpl(private val interfaceGetAllOrdersRepository: InterfaceGetAllOrdersRepository) :
    GetAllOrderInterface {

    override suspend fun getAllOrder(): Flow<PagingData<Order>> =
        interfaceGetAllOrdersRepository.getListOrder()
}