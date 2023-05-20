package com.meridiane.lection3.data.repository

import com.meridiane.lection3.data.entity.AddOrderData
import com.meridiane.lection3.data.entity.product.DetailProduct
import com.meridiane.lection3.data.entity.authorization.AuthorizationDataModel
import com.meridiane.lection3.data.entity.authorization.SignInData
import com.meridiane.lection3.data.entity.order.AddOrderAnswer
import com.meridiane.lection3.data.entity.order.OrderData
import com.meridiane.lection3.data.entity.order.cancel.CancelData
import com.meridiane.lection3.data.entity.product.DataProduct
import com.meridiane.lection3.data.entity.profile.GetProfile
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface CowboysApi {

    @PUT("user/signin")
    suspend fun signIn(@Body params: AuthorizationDataModel): SignInData

    @GET("products")
    suspend fun getProduct(
        @Query("PageNumber") PageNumber: Int,
        @Query("PageSize") PageSize: Int
    ): DataProduct

    @GET("user")
    suspend fun profile(): GetProfile

    @GET("products/{id}")
    suspend fun getProductDetail(
        @Path("id") id: String
    ): DetailProduct

    @GET("orders")
    suspend fun getOrders(
        @Query("PageNumber") PageNumber: Int,
        @Query("PageSize") PageSize: Int
    ): OrderData

    @POST("orders/checkout")
    suspend fun addOrder(@Body params: AddOrderData): AddOrderAnswer

    @PUT("orders/{orderId}")
    suspend fun getCancelOrder(
        @Path("orderId") orderId: String
    ): CancelData
}