package com.meridiane.lection3.domain.repository

interface InterfaceGetToken {

    fun getTokenBd(): String

    suspend fun  getTokenServer(login: String, password: String): String?
}