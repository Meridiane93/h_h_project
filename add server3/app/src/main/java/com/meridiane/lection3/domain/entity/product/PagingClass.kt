package com.meridiane.lection3.domain.entity.product

import com.meridiane.lection3.domain.entity.AllOrder

data class PagingClass(val order: AllOrder){
    val createdAt: String? = order.createdAt
    val deliveryAddress: String?= order.deliveryAddress
    val etd: String?= order.etd
    val id: String?= order.id
    val number: Int?= order.number
    val productId: String?= order.productId
    val productPreview: String?= order.productPreview
    val productQuantity: Int?= order.productQuantity
    val productSize: String?= order.productSize
    val status: String?= order.status
}
