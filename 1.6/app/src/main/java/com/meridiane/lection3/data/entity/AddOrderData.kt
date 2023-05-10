package com.meridiane.lection3.data.entity

data class AddOrderData(
    var id: String ?= null,
    var quantity : Int ?= null,
    var size: String ?= null,
    var house: String ?= null,
    var departament: String ?= null,
    var time: String ?= null
)
