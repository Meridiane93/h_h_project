package com.meridiane.lection3.data.repository

import com.meridiane.lection3.data.entity.order.AddOrderData
import com.meridiane.lection3.data.entity.product.DetailProduct
import com.meridiane.lection3.data.entity.authorization.AuthorizationDataModel
import com.meridiane.lection3.data.entity.authorization.SignInData
import com.meridiane.lection3.data.entity.order.AddOrderEntity
import com.meridiane.lection3.data.entity.order.OrderData
import com.meridiane.lection3.data.entity.order.cancel.CancelData
import com.meridiane.lection3.data.entity.product.DataProduct
import com.meridiane.lection3.data.entity.profile.GetProfile
import com.meridiane.lection3.data.entity.profile.UserProfileChanges
import com.meridiane.lection3.domain.entity.profile.ChangeUserProfileDomain
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
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
    suspend fun addOrder(@Body params: AddOrderData): AddOrderEntity

    @PUT("orders/{orderId}")
    suspend fun getCancelOrder(
        @Path("orderId") orderId: String
    ): CancelData

    @Multipart
    @POST("user/photo")
    suspend fun setPhoto(@Part uploadedFile: MultipartBody.Part?): Response<Unit>

    @GET("user/photo/{fileId}")
    suspend fun getPhoto(@Path("fileId") id:String): Response<ResponseBody>

    @PATCH("user")
    suspend fun userProfileChange(
        @Body changeUserProfileRequest: List<UserProfileChanges>
    ): Response<UserProfileChanges>



}