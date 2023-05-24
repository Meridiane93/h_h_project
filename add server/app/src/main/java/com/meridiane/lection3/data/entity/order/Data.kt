package com.meridiane.lection3.data.entity.order

import com.meridiane.lection3.domain.entity.AddOrderEntityDomain

data class Data(
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
){
    fun toAddOrder(): AddOrderEntityDomain = AddOrderEntityDomain(
        createdAt = createdAt,
        deliveryAddress = deliveryAddress,
        etd = etd,
        id = id,
        number = number,
        productId = productId,
        productPreview = productPreview,
        productQuantity = productQuantity,
        productSize = productSize,
        status = status
    )
}