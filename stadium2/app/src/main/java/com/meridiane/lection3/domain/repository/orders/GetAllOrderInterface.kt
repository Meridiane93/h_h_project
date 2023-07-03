package com.meridiane.lection3.domain.repository.orders

interface GetAllOrderInterface {
    suspend fun getAllOrder(): List<Int>
}