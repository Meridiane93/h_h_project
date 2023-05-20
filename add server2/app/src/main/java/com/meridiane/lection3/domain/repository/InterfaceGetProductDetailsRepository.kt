package com.meridiane.lection3.domain.repository


import com.meridiane.lection3.domain.entity.AddOrder
import com.meridiane.lection3.domain.entity.product_detail.ProductDetail
import kotlinx.coroutines.flow.Flow

interface InterfaceGetProductDetailsRepository {
    suspend fun getDetailsProduct(id:String): Flow<ProductDetail>
    suspend fun addOrder(order: AddOrder): String
}