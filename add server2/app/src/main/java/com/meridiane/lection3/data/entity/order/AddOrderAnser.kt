package com.meridiane.lection3.data.entity.order

data class AddOrderAnswer(
    val errors: Errors?= null,
    val status: Int?= null,
    val title: String?= null,
    val traceId: String?= null,
    val type: String?= null
)