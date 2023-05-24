package com.meridiane.lection3.data.entity.product

import com.meridiane.lection3.domain.entity.product_detail.BadgeProduct

data class Badge(
    val color: String ?= null,
    val value: String ?= null
){
    fun toBadge():BadgeProduct = BadgeProduct(
        color = color,
        value = value
    )
}