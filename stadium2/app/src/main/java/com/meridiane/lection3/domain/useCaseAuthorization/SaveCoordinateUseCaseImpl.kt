package com.meridiane.lection3.domain.useCaseAuthorization

import com.meridiane.lection3.domain.repository.authorization.InterfaceSaveCoordinate

interface SaveCoordinate {
    fun saveCoordinate(coordinate: String)
}

class SaveCoordinateUseCaseImpl(private val interfaceSaveCoordinate: InterfaceSaveCoordinate) :
    SaveCoordinate {

    override fun saveCoordinate(coordinate: String) =
        interfaceSaveCoordinate.saveCoordinate(coordinate)
}