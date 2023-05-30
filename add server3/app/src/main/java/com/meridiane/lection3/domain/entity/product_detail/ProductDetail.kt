package com.meridiane.lection3.domain.entity.product_detail

data class ProductDetail(
    val badge: List<BadgeProduct> ?= null,
    val department: String ?= null,
    val description: String ?= null,
    val details: List<String> ?= null,
    val id: String ?= null,
    val images: List<String> ?= null,
    val preview: String ?= null,
    val price: Int ?= null,
    val sizes: List<SizeProduct> ?= null,
    val title: String ?= null
)