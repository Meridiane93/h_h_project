package com.meridiane.lection3.domain.entity

import com.meridiane.lection3.data.entity.AddOrderData

data class AddOrder(
    var id: String ?= null,
    var quantity : Int ?= null,
    var size: String ?= null,
    var house: String ?= null,
    var departament: String ?= null,
    var time: String ?= null
){
    fun toAddOrderData(): AddOrderData = AddOrderData(
        id = id,
        quantity = quantity,
        size = size,
        house = house,
        departament = departament,
        time = time
    )
}
