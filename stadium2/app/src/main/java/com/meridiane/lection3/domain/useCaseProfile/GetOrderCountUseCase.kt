package com.meridiane.lection3.domain.useCaseProfile

import com.meridiane.lection3.domain.repository.orders.GetAllOrderInterface

interface GetOrderInterface {
    suspend fun allOrder(): List<Int>
}

class GetProductsUseCaseImpl(private val getAllOrderInterface: GetAllOrderInterface)
    : GetOrderInterface {

    override suspend fun allOrder(): List<Int> =
        getAllOrderInterface.getAllOrder()
}