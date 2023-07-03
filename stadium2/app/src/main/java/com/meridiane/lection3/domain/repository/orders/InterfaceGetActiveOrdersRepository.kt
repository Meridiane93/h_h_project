package com.meridiane.lection3.domain.repository.orders

import androidx.paging.PagingData
import com.meridiane.lection3.domain.entity.order.AllOrder
import kotlinx.coroutines.flow.Flow

interface InterfaceGetActiveOrdersRepository {
    fun getOrders(): Flow<PagingData<AllOrder>>
    suspend fun cancelOrderInterface(id: String): AllOrder

}