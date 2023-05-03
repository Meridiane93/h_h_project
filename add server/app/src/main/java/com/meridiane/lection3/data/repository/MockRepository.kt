package com.meridiane.lection3.data.repository

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.meridiane.lection3.R
import com.meridiane.lection3.data.entity.authorization.AuthorizationDataModel
import com.meridiane.lection3.data.entity.authorization.SignInData
import com.meridiane.lection3.data.entity.product.ProductMain
import com.meridiane.lection3.data.pagination.PageSource
import com.meridiane.lection3.data.pagination.PageSourceActiveOrder
import com.meridiane.lection3.data.pagination.PageSourceAllOrder
import com.meridiane.lection3.data.pagination.ProductPageLoader
import com.meridiane.lection3.data.storage.TokenStorage
import com.meridiane.lection3.domain.entity.Order
import com.meridiane.lection3.domain.entity.Product
import com.meridiane.lection3.domain.entity.Profile
import com.meridiane.lection3.domain.repository.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "http://45.144.64.179/cowboys/api/"

class MockRepository(
    private val tokenStorage: TokenStorage
) :
    InterfaceGetLoginRepository,
    InterfaceGetProductsRepository,
    InterfaceGetProfileRepository,
    //InterfaceGetProductDetailsRepository,
    InterfaceGetActiveOrdersRepository,
    InterfaceGetToken,
    InterfaceSaveToken {

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(CowboysApi::class.java)

    override fun getTokenBd(): String = tokenStorage.getToken()

    override fun saveToken(token: String) = tokenStorage.saveToken(token)

    override fun getTokenServer(login: String, password: String): Result.Companion {

        val result = Result

            retrofit.signIn(AuthorizationDataModel(login, password))
                .enqueue(object : Callback<SignInData> {

                    override fun onResponse(
                        call: Call<SignInData>,
                        response: Response<SignInData>
                    ) {
                        if (response.isSuccessful) {

                            result.success(response.body()?.data?.accessToken!!)
                            Log.d("MyTag", "onResponse if ${response.body()?.data?.accessToken!!}")
                        }
                    }

                    override fun onFailure(call: Call<SignInData>, t: Throwable) {
                        result.failure<String>(t)
                        Log.d("MyTag", "onFailure $t")
                    }

                })
            Log.d("MyTag", " if $result")

        return result
    }

    /*
    В дата слое у меня есть сущности: AuthorizationDataModel, Data, OrderDataModel, ProductDataModel, Profile, SignInData
    В домен слое у меня есть сущности: Authorization, Order, Product, Profile

    Одинаковые сущности имеют
    Authorization, Order, Product, Profile и AuthorizationDataModel, OrderDataModel, ProductDataModel, Profile
     */

    // перекидывание с одной сущности в
    // если нам нужно вернуть класс который внутри слоя домен
    // то val product = Product() заполняем и возвращаем

    // old code

    override suspend fun getProfile(): Result<Profile> {
        delay(3000L)
        return randomResultProfile(
            Profile(
                "Анна",
                "Виноградова",
                "Садовник",
                "https://www.gravatar.com/avatar/205e460b479e2e5b48aec07710c08d50?s=200"
            )
        )
    }

//    override suspend fun getDetailsProduct(id: String): Flow<Product> = flow {
//        getProducts3().forEach { if (it.id == id) emit(it) }
//    }

    override fun getProducts2(): Flow<PagingData<Product>> {

        val loader: ProductPageLoader = { pageIndex, pageSize ->
            getProductServer(pageIndex, pageSize)
        }
        return Pager(
            config = PagingConfig(
                pageSize = 2,
                enablePlaceholders = false,
            ),
            pagingSourceFactory = { PageSource( 3,loader) }
        ).flow
    }

    private suspend fun getProductServer(pageIndex: Int, pageSize: Int): List<Product>  {

        val result = Result

            val offset = pageIndex * pageSize
            val list = retrofit.getProduct()
                .enqueue(object : Callback<ProductMain> {

                    override fun onResponse(call: Call<ProductMain>, response: Response<ProductMain>) {
                        if (response.isSuccessful) {
                            result.success(response.body()?.description)

                            Log.d("MyTag","$result")
                        }
                    }

                    override fun onFailure(call: Call<ProductMain>, t: Throwable) {
                        result.failure<String>(t)
                        Log.d("MyTag","onFailure")
                    }

                })
        Log.d("MyTag","list $list")
        return listOf(
            Product(
                "1",
                "Футболка серая",
                "Футболка ",
                R.drawable.reposit_id1
            ),
            Product(
                "1",
                "Футболка серая",
                "Футболка ",
                R.drawable.reposit_id1
            ))

    }


//    override suspend fun getProducts3(): List<Product> {
//
//
//        return listOf(
//            Product(
//                "1",
//                "Футболка серая",
//                "Футболка ",
//                R.drawable.reposit_id1
//            )
//        )
//    }

    override suspend fun getAllOrder(): List<Order> {
        randomDelay()
        return listOf(
            Order(
                "1",
                "Заказ №123 от 19.09.21 18:03",
                "В работе",
                "4 × M • Nike Tampa Bay Buccaneers Super Bowl LV",
                "Дата доставки: 24.09.21 в 16:00",
                "Адрес доставки: г. Саранск, ул. Демократическая, 14",
                R.drawable.reposit_id1
            ),
            Order(
                "2",
                "Заказ №123 от 19.09.21 18:03",
                "В работе",
                "4 × M • Nike Tampa Bay Buccaneers Super Bowl LV",
                "Дата доставки: 24.09.21 в 16:00",
                "Адрес доставки: г. Саранск, ул. Демократическая, 14",
                R.drawable.reposit_id2
            ),
            Order(
                "3",
                "Заказ №123 от 19.09.21 18:03",
                "В работе",
                "4 × M • Nike Tampa Bay Buccaneers Super Bowl LV",
                "Дата доставки: 24.09.21 в 16:00",
                "Адрес доставки: г. Саранск, ул. Демократическая, 14",
                R.drawable.reposit_id3
            ),
            Order(
                "4",
                "Заказ №123 от 19.09.21 18:03",
                "В работе",
                "4 × M • Nike Tampa Bay Buccaneers Super Bowl LV",
                "Дата доставки: 24.09.21 в 16:00",
                "Адрес доставки: г. Саранск, ул. Демократическая, 14",
                R.drawable.reposit_id4
            )
        )
    }

    override suspend fun getActiveOrder(): List<Order> {
        randomDelay()
        return listOf(
            Order(
                "1",
                "Заказ №123 от 19.09.21 18:03",
                "В работе",
                "4 × M • Nike Tampa Bay Buccaneers Super Bowl LV",
                "Дата доставки: 24.09.21 в 16:00",
                "Адрес доставки: г. Саранск, ул. Демократическая, 14",
                R.drawable.reposit_id1
            )
        )
    }

    override suspend fun getSizeAllOrder(): Int =
        getAllOrder().size


    override suspend fun getSizeActiveOrder(): Int =
        getActiveOrder().size


    override fun getList(): Flow<PagingData<Product>> {
        val loader: ProductPageLoader = { pageIndex, pageSize ->
            getProductServer(pageIndex, pageSize)
        }
        return Pager(
            config = PagingConfig(
                pageSize = 2,
                enablePlaceholders = false,
            ),
            pagingSourceFactory = { PageSource( 3,loader) }
        ).flow
    }

    override fun getListOrder(): Flow<PagingData<Order>> {
        return Pager(
            config = PagingConfig(
                pageSize = 2,
                enablePlaceholders = false,
                initialLoadSize = 4,
                prefetchDistance = 2 / 2
            ),
            pagingSourceFactory = {
                PageSourceAllOrder(this)
            }
        ).flow
    }

    override fun getListActiveOrder(): Flow<PagingData<Order>> {
        return Pager(
            config = PagingConfig(
                pageSize = 2,
                enablePlaceholders = false,
                initialLoadSize = 2,
                prefetchDistance = 2 / 2
            ),
            pagingSourceFactory = {
                PageSourceActiveOrder(this)
            }
        ).flow
    }

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

    private fun <T> randomResultProfile(data: T): Result<T> =
        if ((0..100).random() < 99) {
            Result.success(data)
        } else {
            Result.failure(RuntimeException())
        }

}

