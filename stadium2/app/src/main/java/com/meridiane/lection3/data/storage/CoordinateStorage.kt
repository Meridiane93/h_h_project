package com.meridiane.lection3.data.storage

interface CoordinateStorage {

    fun getCoordinate(): String

    fun saveCoordinate(coordinate: String)

    fun clearCoordinate()
}