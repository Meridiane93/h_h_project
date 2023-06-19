package com.meridiane.lection3.domain.entity.profile

import com.meridiane.lection3.data.entity.profile.UserProfileChanges

class ChangeUserProfileDomain (
    val path: String ?= null,
    private val op: String ?= null,
    val value: String?= null,
){
    fun toUserProfileChanges(): UserProfileChanges = UserProfileChanges(
        path = path,
        op = op,
        value = value
    )
}