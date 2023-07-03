package com.meridiane.lection3.domain.repository.products


import com.meridiane.lection3.domain.entity.order.AddOrder
import com.meridiane.lection3.domain.entity.order.AddOrderEntityDomain
import com.meridiane.lection3.domain.entity.product_detail.ProductDetail
import kotlinx.coroutines.flow.Flow

interface InterfaceGetProductDetailsRepository {
    suspend fun getDetailsProduct(id:String): Flow<ProductDetail>
    suspend fun addOrder(order: AddOrder): AddOrderEntityDomain
}