package com.meridiane.lection3.data.entity.order

data class Errors(
    val ProductId: List<String>?= null,
    val order: List<String>?= null
)