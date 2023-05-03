package com.meridiane.lection3.data.entity.product

data class ProductMain(
    val badge: List<Badge>,
    val department: String,
    val description: String,
    val details: List<String>,
    val id: String,
    val images: List<String>,
    val preview: String,
    val price: Int,
    val sizes: List<Size>,
    val title: String
)