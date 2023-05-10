package com.meridiane.lection3.data.entity.authorization

data class Profile(
    val avatarId: String ?= null,
    val name: String ?= null,
    val occupation: String ?= null,
    val surname: String ?= null
)