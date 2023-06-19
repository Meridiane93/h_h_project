package com.meridiane.lection3.domain.useCaseAuthorization

import com.meridiane.lection3.domain.repository.authorization.InterfaceClearToken

interface ClearToken {
    fun clearToken()
}

class ClearTokenUseCaseImpl(private val interfaceClearToken: InterfaceClearToken) : ClearToken {
    override fun clearToken() = interfaceClearToken.clearTokenBd()
}