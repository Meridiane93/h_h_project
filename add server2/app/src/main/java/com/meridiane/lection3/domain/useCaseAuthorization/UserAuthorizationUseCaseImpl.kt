package com.meridiane.lection3.domain.useCaseAuthorization

import com.meridiane.lection3.domain.entity.Authorization

import com.meridiane.lection3.domain.repository.InterfaceGetLoginRepository

interface UserAuthorization {
    suspend fun authorization(authorization: Authorization): Result<String>
}

class UserAuthorizationUseCaseImpl(private val interfaceGetLoginRepository: InterfaceGetLoginRepository) :
    UserAuthorization {

    override suspend fun authorization(authorization: Authorization): Result<String> =
        interfaceGetLoginRepository.getLogin()

}