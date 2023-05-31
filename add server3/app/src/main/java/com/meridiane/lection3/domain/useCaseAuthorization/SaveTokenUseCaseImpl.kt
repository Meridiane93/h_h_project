package com.meridiane.lection3.domain.useCaseAuthorization

import com.meridiane.lection3.domain.repository.authorization.InterfaceSaveToken

interface SaveToken {
    fun saveToken(token: String)
}

class SaveTokenUseCaseImpl(private val interfaceSaveToken: InterfaceSaveToken) :
    SaveToken {

    override fun saveToken(token: String) = interfaceSaveToken.saveToken(token)

}