package com.meridiane.lection3.data.entity.authorization

data class Data(
    val accessToken: String ?= null,
    val profile: Profile ?= null
)