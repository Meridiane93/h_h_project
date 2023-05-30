package com.meridiane.lection3.domain.entity

import com.meridiane.lection3.data.entity.AddOrderData

data class AddOrder(
    var ProductId: String ?= null,
    var Quantity : Int ?= null,
    var Size: String ?= null,
    var House: String ?= null,
    var Apartment: String ?= null,
    var Etd: String ?= null
){
    fun toAddOrderData(): AddOrderData = AddOrderData(
        ProductId  = ProductId,
        Quantity = Quantity,
        Size = Size,
        House  = House,
        Apartment  = Apartment,
        Etd = Etd
    )
}

