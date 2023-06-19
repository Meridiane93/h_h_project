package com.meridiane.lection3.domain.useCaseAuthorization

import com.meridiane.lection3.domain.repository.authorization.InterfaceGetToken

interface GetToken {
    fun getTokenBd(): String

    suspend fun getTokenServer(login: String, password: String): Result<String?>
}

class GetTokenUseCaseImpl(private val interfaceGetToken: InterfaceGetToken) :
    GetToken {

    override fun getTokenBd(): String = interfaceGetToken.getTokenBd()

    override suspend fun getTokenServer(login: String, password: String): Result<String?> {

        val answer = interfaceGetToken.getTokenServer(login, password)

       return try {
            Result.success(answer)
        }
        catch (e:Exception){
            Result.failure(e)
        }
    }
}