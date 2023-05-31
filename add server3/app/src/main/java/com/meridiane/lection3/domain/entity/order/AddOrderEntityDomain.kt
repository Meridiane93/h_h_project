package com.meridiane.lection3.domain.entity.order

data class AddOrderEntityDomain(
    val createdAt: String,
    val deliveryAddress: String,
    val etd: String,
    val id: String,
    val number: Int,
    val productId: String,
    val productPreview: String,
    val productQuantity: Int,
    val productSize: String,
    val status: String
)
