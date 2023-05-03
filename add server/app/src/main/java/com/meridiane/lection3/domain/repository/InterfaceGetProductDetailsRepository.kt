package com.meridiane.lection3.domain.repository


import com.meridiane.lection3.domain.entity.Product
import kotlinx.coroutines.flow.Flow

interface InterfaceGetProductDetailsRepository {
    suspend fun getDetailsProduct(id:String): Flow<Product>
}