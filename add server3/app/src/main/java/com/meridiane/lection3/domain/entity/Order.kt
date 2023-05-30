package com.meridiane.lection3.domain.entity

data class Order(
    val id: String? = null,
    val numberOrder: String? = null,
    val status: String? = null,
    val size: String? = null,
    val dateGet: String? = null,
    val address: String? = null,
    val image: Int? = null
)
