package com.meridiane.lection3.domain.repository

import androidx.paging.PagingData
import com.meridiane.lection3.domain.models.Product
import kotlinx.coroutines.flow.Flow

interface InterfaceGetProductsRepository {
    suspend fun getProducts2(): List<Product>

    fun getList(): Flow<PagingData<Product>>
}