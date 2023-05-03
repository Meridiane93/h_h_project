package com.meridiane.lection3.domain.repository

import androidx.paging.PagingData
import com.meridiane.lection3.domain.entity.Product
import kotlinx.coroutines.flow.Flow


interface InterfaceGetProductsRepository {
    fun getProducts2(): Flow<PagingData<Product>>

    fun getList():Flow<PagingData<Product>>
}