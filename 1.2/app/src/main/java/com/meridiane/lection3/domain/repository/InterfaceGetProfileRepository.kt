package com.meridiane.lection3.domain.repository

import com.meridiane.lection3.domain.models.Profile

interface InterfaceGetProfileRepository {
    suspend fun getProfile(): Result<Profile>
}