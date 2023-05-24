package com.meridiane.lection3.data.entity.profile

import com.meridiane.lection3.domain.entity.Profile

data class DataProfile(
    val avatarId: String ?= null,
    val name: String ?= null,
    val occupation: String ?= null,
    val surname: String ?= null
) {
    fun toProfile(): Profile = Profile(
        name = name,
        surname = surname,
        occupation = occupation,
        avatarUrl = avatarId
    )
}