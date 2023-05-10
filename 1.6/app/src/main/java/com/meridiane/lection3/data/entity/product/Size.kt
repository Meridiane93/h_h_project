package com.meridiane.lection3.data.entity.product

import com.meridiane.lection3.domain.entity.product_detail.SizeProduct

data class Size(
    val isAvailable: Boolean ?= null,
    val value: String ?= null
){
    fun toSize(): SizeProduct = SizeProduct(
        isAvailable = isAvailable,
        value = value
    )
}
