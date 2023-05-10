package com.meridiane.lection3.data.entity.order

data class AddOrderAnswer(
    val errors: Errors,
    val status: Int,
    val title: String,
    val traceId: String,
    val type: String
)