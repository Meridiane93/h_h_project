package com.meridiane.lection3.domain.repository

import androidx.paging.PagingData
import com.meridiane.lection3.domain.entity.product.Product
import com.meridiane.lection3.domain.entity.product_detail.ProductDetail
import kotlinx.coroutines.flow.Flow

interface InterfaceGetProductsRepository {
    fun getProducts(): Flow<PagingData<Product>>

    fun getList():Flow<PagingData<Product>>
}