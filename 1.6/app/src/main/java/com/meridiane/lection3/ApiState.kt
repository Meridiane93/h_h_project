package com.meridiane.lection3

import com.meridiane.lection3.domain.entity.Profile

//sealed class ApiState<out T>
//object Loading : ApiState<Nothing>()
//data class Success<out T>(val data: T) : ApiState<T>()
//data class Error(val throwable: Throwable) : ApiState<Nothing>()

sealed class ApiState<out T> {
    object Loading : ApiState<Nothing>()
    data class Success(val data: Profile) : ApiState<Nothing>()
    data class Error(val data: String) : ApiState<Nothing>()
}
