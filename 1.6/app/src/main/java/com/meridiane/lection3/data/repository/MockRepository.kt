package com.meridiane.lection3.data.repository

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.meridiane.lection3.Constants
import com.meridiane.lection3.R
import com.meridiane.lection3.data.entity.authorization.AuthorizationDataModel
import com.meridiane.lection3.data.entity.product.ProductMain
import com.meridiane.lection3.data.pagination.OrdersPageLoader
import com.meridiane.lection3.data.pagination.PageSource
import com.meridiane.lection3.data.pagination.PageSourceAllOrder
import com.meridiane.lection3.data.pagination.ProductPageLoader
import com.meridiane.lection3.data.storage.TokenStorage
import com.meridiane.lection3.domain.entity.AddOrder
import com.meridiane.lection3.domain.entity.Order
import com.meridiane.lection3.domain.entity.product.Product
import com.meridiane.lection3.domain.entity.Profile
import com.meridiane.lection3.domain.entity.product_detail.ProductDetail
import com.meridiane.lection3.domain.repository.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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

            val offset = pageIndex * pageSize
            val list = retrofit.getProduct(pageIndex,pageSize).data

            return@withContext list!!.map(ProductMain::toProduct)
        }

    // получить список заказов
    override suspend fun getOrders(): Flow<PagingData<Order>> {

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
    private suspend fun getOrdersServer(pageIndex: Int, pageSize: Int): List<Order> =
        withContext(Dispatchers.IO) {

            val offset = pageIndex * pageSize
            val list = retrofit.getOrders(offset,pageSize).data
            return@withContext list!!.map(ProductMain::toOrder)
        }

    // получить данные профиля
    override suspend fun getProfile(): Profile =
        retrofit.profile().data?.profile!!.toProfile()

    // получить детали продукта
    override suspend fun getDetailsProduct(id: String): Flow<ProductDetail> = flow {
       emit(retrofit.getProductDetail(id).data!!.toProductDetail())
    }

    override suspend fun addOrder(order: AddOrder): String =
      retrofit.addOrder(order.toAddOrderData()).title


//    override fun getActiveOrder(): Flow<PagingData<Order>> {
//        val loader: ProductPageLoader = { pageIndex, pageSize ->
//            getProductServer(pageIndex, pageSize)
//        }
//        return Pager(
//            config = PagingConfig(
//                pageSize = 2,
//                enablePlaceholders = false,
//            ),
//            pagingSourceFactory = { PageSourceAllOrder(Constants.PAGE_SIZE, loader) }
//        ).flow
//    }




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

//    override fun getListOrder(): Flow<PagingData<Order>> {
//        return Pager(
//            config = PagingConfig(
//                pageSize = 2,
//                enablePlaceholders = false,
//                initialLoadSize = 4,
//                prefetchDistance = 2 / 2
//            ),
//            pagingSourceFactory = {
//                PageSourceAllOrder(this)
//            }
//        ).flow
//    }

//    override fun getListActiveOrder(): Flow<PagingData<Order>> {
//        return Pager(
//            config = PagingConfig(
//                pageSize = 2,
//                enablePlaceholders = false,
//                initialLoadSize = 2,
//                prefetchDistance = 2 / 2
//            ),
//            pagingSourceFactory = {
//                PageSourceActiveOrder(this)
//            }
//        ).flow
//    }

    override suspend fun getLogin(): Result<String> {
        delay(3000L)
        return randomResult2("is success")
    }

    private suspend fun randomDelay() {
        delay((1000L..2000L).random())
    }

    private fun <T> randomResult2(data: T): Result<T> =
        if ((0..100).random() < 90) {
            Result.failure(RuntimeException())
        } else {
            Result.success(data)
        }

}

