package com.meridiane.lection3.domain.useCaseAuthorization

import com.meridiane.lection3.domain.repository.authorization.InterfaceClearCoordinate

interface ClearCoordinate {
    fun clearCoordinate()
}

class ClearCoordinateUseCaseImpl(private val interfaceClearCoordinate: InterfaceClearCoordinate) : ClearCoordinate {
    override fun clearCoordinate() = interfaceClearCoordinate.clearCoordinateBd()
}