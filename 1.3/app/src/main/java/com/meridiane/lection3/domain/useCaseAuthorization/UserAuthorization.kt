package com.meridiane.lection3.domain.useCaseAuthorization

import com.meridiane.lection3.domain.models.Authorization

import com.meridiane.lection3.domain.repository.InterfaceGetLoginRepository

class UserAuthorization(private val interfaceGetLoginRepository: InterfaceGetLoginRepository) {

    suspend fun authorization(authorization: Authorization): Result<String> =
        interfaceGetLoginRepository.getLogin()

}