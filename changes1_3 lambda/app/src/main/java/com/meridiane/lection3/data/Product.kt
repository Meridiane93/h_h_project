package com.meridiane.lection3.data

//sealed class ProductState<R> {

//    class Loading<R> : ProductState<R>()
 //   class Success<R>(val data: R) : ProductState<R>()
//    class Error<R>(val throwable: Throwable) : ProductState<R>()

//}

data class Product(
    val id: String? = null,
    val title: String? = null,
    val category: String? = null,
    val preview: Int? = null
)