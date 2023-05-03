package com.meridiane.lection3.data.repository

import com.meridiane.lection3.data.entity.authorization.AuthorizationDataModel
import com.meridiane.lection3.data.entity.authorization.SignInData
import com.meridiane.lection3.data.entity.product.ProductMain
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT

interface CowboysApi {

    @PUT("user/signin")
    fun signIn(@Body params: AuthorizationDataModel): Call<SignInData>

    @GET("products?PageNumber=1&PageSize=3")
    fun getProduct(
//        @Query("PageNumber") PageNumber: Int,
//        @Query("PageSize") PageSize: Int
    ): Call<ProductMain>
}