package com.meridiane.lection3.data.repository

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.core.net.toFile
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.meridiane.lection3.Constants
import com.meridiane.lection3.data.entity.authorization.AuthorizationDataModel
import com.meridiane.lection3.data.entity.order.DataAllOrder
import com.meridiane.lection3.data.entity.product.ProductMain
import com.meridiane.lection3.data.pagination.OrdersPageLoader
import com.meridiane.lection3.data.pagination.PageSource
import com.meridiane.lection3.data.pagination.PageSourceAllOrder
import com.meridiane.lection3.data.pagination.ProductPageLoader
import com.meridiane.lection3.data.storage.TokenStorage
import com.meridiane.lection3.domain.entity.AddOrder
import com.meridiane.lection3.domain.entity.AddOrderEntityDomain
import com.meridiane.lection3.domain.entity.AllOrder
import com.meridiane.lection3.domain.entity.product.Product
import com.meridiane.lection3.domain.entity.Profile
import com.meridiane.lection3.domain.entity.product_detail.ProductDetail
import com.meridiane.lection3.domain.repository.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Multipart
import java.io.File

class MockRepository(
    private val tokenStorage: TokenStorage
) :
    InterfaceGetLoginRepository,
    InterfaceGetProductsRepository,
    InterfaceGetProfileRepository,
    InterfaceGetActiveOrdersRepository,
    InterfaceGetToken,
    InterfaceGetProductDetailsRepository,
    InterfaceSaveToken {

    // добавление токена в запрос
    private val client = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val original = chain.request()

            val requestBuilder = original.newBuilder()
                .header("Authorization", "Bearer ${Constants.KEY}")
                .method(original.method(), original.body())
            val request = requestBuilder.build()
            chain.proceed(request)
        }.build()

    //создание ретрофита
    private val retrofit = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(CowboysApi::class.java)

    // получить токен из преференс
    override fun getTokenBd(): String = tokenStorage.getToken()

    // сохранение токена
    override fun saveToken(token: String) = tokenStorage.saveToken(token)

    // получить токен из сервера
    override suspend fun getTokenServer(login: String, password: String): String? =
        retrofit.signIn(AuthorizationDataModel(login, password)).data?.accessToken

    // получить данные профиля
    override fun getProducts(): Flow<PagingData<Product>> {

        val loader: ProductPageLoader = { pageIndex, pageSize ->
            getProductServer(pageIndex, pageSize)
        }
        return Pager(
            config = PagingConfig(
                pageSize = 2,
                enablePlaceholders = false,
            ),
            pagingSourceFactory = { PageSource(Constants.PAGE_SIZE, loader) }
        ).flow
    }

    // получить список продуктов
    private suspend fun getProductServer(pageIndex: Int, pageSize: Int): List<Product> =
        withContext(Dispatchers.IO) {
            val list = retrofit.getProduct(pageIndex,pageSize).data

            return@withContext list!!.map(ProductMain::toProduct)
        }

    // получить список заказов
    override fun getOrders(): Flow<PagingData<AllOrder>> {

        val loader: OrdersPageLoader = { pageIndex, pageSize ->
            getOrdersServer(pageIndex, pageSize)
        }
        return Pager(
            config = PagingConfig(
                pageSize = 2,
                enablePlaceholders = false,
                prefetchDistance = 2 / 2
            ),
            pagingSourceFactory = { PageSourceAllOrder(Constants.PAGE_SIZE, loader) }
        ).flow
        }

    // получить список заказов из сервера
    private suspend fun getOrdersServer(pageIndex: Int, pageSize: Int): List<AllOrder> =
        withContext(Dispatchers.IO) {
            val list = retrofit.getOrders(pageIndex,pageSize).data
            return@withContext list!!.map(DataAllOrder::toAllOrder)
        }

    // получить данные профиля
    override suspend fun getProfile(): Profile =
        retrofit.profile().data?.profile!!.toProfile()

    // получить детали продукта
    override suspend fun getDetailsProduct(id: String): Flow<ProductDetail> = flow {
       emit(retrofit.getProductDetail(id).data!!.toProductDetail())
    }

    // добавить заказ на сервер
    override suspend fun addOrder(order: AddOrder): AddOrderEntityDomain =
        retrofit.addOrder(order.toAddOrderData()).data.toAddOrder()

    // отменить заказ на сервере
    override suspend fun cancelOrderInterface(id: String): AllOrder =
        retrofit.getCancelOrder(id).data.toAllOrder()

    override suspend fun setPhoto(photo: String) {

        val file = File(photo)
        val filePart:MultipartBody.Part = MultipartBody.Part.createFormData("uploadedFile", file.name,
            RequestBody.create(
            MediaType.parse("image/jpeg"), file))


       val answer =  retrofit.setPhoto(filePart)
        Log.d("MyTag", "answer: ${answer.body().toString()}")
    }

    override suspend fun getPhoto(id: String): Bitmap? {
        return try {
            BitmapFactory.decodeStream(
                retrofit.getPhoto(id).body()?.byteStream()
            )
        }catch (e: Exception){
            null
        }
    }










    override fun getList(): Flow<PagingData<Product>> {
        val loader: ProductPageLoader = { pageIndex, pageSize ->
            getProductServer(pageIndex, pageSize)
        }
        return Pager(
            config = PagingConfig(
                pageSize = 2,
                enablePlaceholders = false,
            ),
            pagingSourceFactory = { PageSource(3, loader) }
        ).flow
    }


    override suspend fun getLogin(): Result<String> {
        delay(3000L)
        return randomResult2("is success")
    }

    private fun <T> randomResult2(data: T): Result<T> =
        if ((0..100).random() < 90) {
            Result.failure(RuntimeException())
        } else {
            Result.success(data)
        }

}

