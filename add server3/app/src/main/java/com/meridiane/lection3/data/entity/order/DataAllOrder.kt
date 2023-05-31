package com.meridiane.lection3.data.entity.order

import com.meridiane.lection3.domain.entity.order.AllOrder

data class DataAllOrder(
    val createdAt: String? = null,
    val deliveryAddress: String? = null,
    val etd: String? = null,
    val id: String? = null,
    val number: Int? = null,
    val productId: String? = null,
    val productPreview: String? = null,
    val productQuantity: Int? = null,
    val productSize: String? = null,
    val status: String? = null
) {

    fun toAllOrder(): AllOrder = AllOrder(
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