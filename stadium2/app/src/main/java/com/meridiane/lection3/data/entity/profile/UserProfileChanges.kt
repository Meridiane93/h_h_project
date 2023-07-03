package com.meridiane.lection3.data.entity.profile

import com.meridiane.lection3.domain.entity.profile.ChangeUserProfileDomain

data class UserProfileChanges(
    val path: String ?= null,
    private val op: String ?= null,
    val value: String ?= null,
){
    fun toChangeUserProfileDomain(): ChangeUserProfileDomain = ChangeUserProfileDomain(
        path = path,
        op = op,
        value = value
    )
}