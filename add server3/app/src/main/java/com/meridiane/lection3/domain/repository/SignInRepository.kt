package com.meridiane.lection3.domain.repository


import com.meridiane.lection3.domain.entity.Authorization

interface SignInRepository {
    fun getSignIn(login: String, password: String): Authorization
}