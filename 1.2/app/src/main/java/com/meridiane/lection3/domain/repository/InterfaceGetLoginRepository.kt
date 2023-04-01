package com.meridiane.lection3.domain.repository

interface InterfaceGetLoginRepository {
    suspend fun getLogin(): Result<String>
}