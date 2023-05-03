package com.meridiane.lection3.domain.repository

import androidx.paging.PagingData
import com.meridiane.lection3.domain.entity.Order
import kotlinx.coroutines.flow.Flow

interface InterfaceGetActiveOrdersRepository {
    suspend fun getActiveOrder(): List<Order>

    suspend fun getAllOrder(): List<Order>

    fun getListOrder(): Flow<PagingData<Order>>
    fun getListActiveOrder(): Flow<PagingData<Order>>

    suspend fun getSizeAllOrder() : Int
    suspend fun getSizeActiveOrder() : Int
}