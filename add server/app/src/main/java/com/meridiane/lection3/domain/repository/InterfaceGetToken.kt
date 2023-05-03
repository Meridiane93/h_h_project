package com.meridiane.lection3.domain.repository

interface InterfaceGetToken {

    fun getTokenBd(): String

    fun getTokenServer(login: String, password: String): Result.Companion
}