package com.meridiane.lection3.domain.repository.authorization


import com.meridiane.lection3.domain.entity.order.Authorization

interface SignInRepository {
    fun getSignIn(login: String, password: String): Authorization
}