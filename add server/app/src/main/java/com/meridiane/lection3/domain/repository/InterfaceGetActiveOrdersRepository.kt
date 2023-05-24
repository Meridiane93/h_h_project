package com.meridiane.lection3.domain.repository

import androidx.paging.PagingData
import com.meridiane.lection3.domain.entity.AllOrder
import kotlinx.coroutines.flow.Flow

interface InterfaceGetActiveOrdersRepository {

    suspend fun getOrders(): Flow<PagingData<AllOrder>>
    suspend fun cancelOrderInterface(id: String): AllOrder

}