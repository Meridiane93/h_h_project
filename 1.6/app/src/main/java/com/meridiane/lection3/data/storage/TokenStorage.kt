package com.meridiane.lection3.data.storage

interface TokenStorage {

    fun getToken(): String

    fun saveToken(token: String)
}