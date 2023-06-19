package com.meridiane.lection3.domain.entity.order

data class AllOrder(
    val createdAt: String?= null,
    val deliveryAddress: String?= null,
    val etd: String?= null,
    val id: String?= null,
    val number: Int?= null,
    val productId: String?= null,
    val productPreview: String?= null,
    val productQuantity: Int?= null,
    val productSize: String?= null,
    val status: String?= null
)