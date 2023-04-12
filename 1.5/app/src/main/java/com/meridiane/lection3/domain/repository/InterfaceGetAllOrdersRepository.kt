package com.meridiane.lection3.domain.repository

import androidx.paging.PagingData
import com.meridiane.lection3.domain.models.Order
import kotlinx.coroutines.flow.Flow

interface InterfaceGetAllOrdersRepository {
    suspend fun getAllOrder(): List<Order>

    fun getListOrder(): Flow<PagingData<Order>>
}