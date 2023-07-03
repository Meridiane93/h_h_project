package com.meridiane.lection3.domain.useCaseAuthorization

import com.meridiane.lection3.domain.repository.authorization.InterfaceGetCoordinate

interface GetCoordinate {
    fun getCoordinateBd(): String
}

class GetCoordinateUseCaseImpl(private val interfaceGetCoordinate: InterfaceGetCoordinate) :
    GetCoordinate {

    override fun getCoordinateBd(): String = interfaceGetCoordinate.getCoordinateBd()
}