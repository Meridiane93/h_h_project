package com.meridiane.lection3.data.entity

data class AddOrderData(
    var ProductId: String ?= null,
    var Quantity : Int ?= null,
    var Size: String ?= null,
    var House: String ?= null,
    var Apartment: String ?= null,
    var Etd: String ?= null
)
