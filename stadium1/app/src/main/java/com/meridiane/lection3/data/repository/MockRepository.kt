package com.meridiane.lection3.data.repository

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
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
import com.meridiane.lection3.domain.entity.order.AddOrder
import com.meridiane.lection3.domain.entity.order.AddOrderEntityDomain
import com.meridiane.lection3.domain.entity.order.AllOrder
import com.meridiane.lection3.domain.entity.product.Product
import com.meridiane.lection3.domain.entity.product_detail.ProductDetail
import com.meridiane.lection3.domain.entity.profile.ChangeUserProfileDomain
import com.meridiane.lection3.domain.entity.profile.Profile
import com.meridiane.lection3.domain.repository.authorization.InterfaceClearToken
import com.meridiane.lection3.domain.repository.authorization.InterfaceGetToken
import com.meridiane.lection3.domain.repository.authorization.InterfaceSaveToken
import com.meridiane.lection3.domain.repository.orders.InterfaceGetActiveOrdersRepository
import com.meridiane.lection3.domain.repository.orders.GetAllOrderInterface
import com.meridiane.lection3.domain.repository.products.InterfaceGetProductDetailsRepository
import com.meridiane.lection3.domain.repository.products.InterfaceGetProductsRepository
import com.meridiane.lection3.domain.repository.profile.InterfaceGetProfileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MockRepository(
    private val tokenStorage: TokenStorage
) :
    InterfaceGetProductsRepository,
    InterfaceGetProfileRepository,
    InterfaceGetActiveOrdersRepository,
    InterfaceGetToken,
    InterfaceGetProductDetailsRepository,
    InterfaceSaveToken,
    InterfaceClearToken,
    GetAllOrderInterface {

    // добавление токена в запрос
    private val client = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val original = chain.request()

            val requestBuilder = original.newBuilder()
                .header("Authorization", "Bearer ${Constants.KEY}")
                .method(original.method, original.body)
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

    // удаление токена
    override fun clearTokenBd() = tokenStorage.clearToken()

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
                pageSize = 5,
                enablePlaceholders = false,
            ),
            pagingSourceFactory = { PageSource(loader) }
        ).flow
    }

    // получить список продуктов
    private suspend fun getProductServer(pageIndex: Int, pageSize: Int): List<Product> =
        withContext(Dispatchers.IO) {
            val list = retrofit.getProduct(pageIndex, pageSize).data

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
            pagingSourceFactory = { PageSourceAllOrder(loader) }
        ).flow
    }

    // получить весь список для табов, костыльное решение
    override suspend fun getAllOrder(): List<Int> {
        var activeCount = 0
        var fullListOrder = 12
        var sizeList = 2
        while (sizeList > 1) {
            val list = retrofit.getOrders(sizeList, 8).data

            if (list?.isEmpty() == true) sizeList = list.size

            sizeList++
            fullListOrder += list?.size ?: 0

            list?.forEach { active ->
                if (active.status == "in_work") activeCount++
            }
        }

        return listOf(fullListOrder, activeCount)
    }

    // получить список заказов из сервера
    private suspend fun getOrdersServer(pageIndex: Int, pageSize: Int): List<AllOrder> =
        withContext(Dispatchers.IO) {

            val list = retrofit.getOrders(pageIndex, pageSize).data
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

    // добавить фото на серер
    override suspend fun setPhoto(photo: MultipartBody.Part) {

        try {
            val response = retrofit.setPhoto(photo)
            if (response.isSuccessful) {
                Log.d("MyTag", "File uploaded successfully!")
            } else {
                Log.e("MyTag", "Failed to upload file. Error code: ${response.code()}")
            }
        } catch (e: Exception) {
            Log.e("MyTag", "Exception occurred: $e")
        }

    }

    // получить фото
    override suspend fun getPhoto(id: String): Bitmap? {
        return try {
            BitmapFactory.decodeStream(
                retrofit.getPhoto(id).body()?.byteStream()
            )
        } catch (e: Exception) {
            null
        }
    }

    // изменить профиль
    override suspend fun changeUserProfile(userData: List<ChangeUserProfileDomain>): ChangeUserProfileDomain =
        retrofit.userProfileChange(userData.map { it.toUserProfileChanges() }).body()!!
            .toChangeUserProfileDomain()

}

