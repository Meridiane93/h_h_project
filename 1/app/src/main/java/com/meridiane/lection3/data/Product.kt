package com.meridiane.lection3.data

//sealed class ProductState<out T>

//object Loading : ProductState<Nothing>()
//data class Success<out T>(val data: T) : ProductState<T>()
//data class Error(val throwable: Throwable) : ProductState<Nothing>()

// data class ProductState(
//    val list : List<Product> ?= null
//)

data class Product(
    val id: String? = null,
    val title: String? = null,
    val category: String? = null,
    val preview: Int? = null
)