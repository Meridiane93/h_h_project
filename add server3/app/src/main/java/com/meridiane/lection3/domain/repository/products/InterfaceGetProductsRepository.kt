package com.meridiane.lection3.domain.repository.products

import androidx.paging.PagingData
import com.meridiane.lection3.domain.entity.product.Product
import kotlinx.coroutines.flow.Flow

interface InterfaceGetProductsRepository {
    fun getProducts(): Flow<PagingData<Product>>
}