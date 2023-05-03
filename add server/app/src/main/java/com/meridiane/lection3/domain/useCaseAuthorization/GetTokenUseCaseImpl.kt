package com.meridiane.lection3.domain.useCaseAuthorization

import com.meridiane.lection3.domain.repository.InterfaceGetToken

interface GetToken {
    fun getTokenBd(): String

    fun getTokenServer(login: String, password: String): Result.Companion
}

class GetTokenUseCaseImpl(private val interfaceGetToken: InterfaceGetToken) :
    GetToken {

    override fun getTokenBd(): String = interfaceGetToken.getTokenBd()

    override fun getTokenServer(login: String, password: String): Result.Companion =
        interfaceGetToken.getTokenServer(login, password)

}