package com.meridiane.lection3.domain.repository

import com.meridiane.lection3.domain.entity.Profile

interface InterfaceGetProfileRepository {
    suspend fun getProfile(): Profile
}