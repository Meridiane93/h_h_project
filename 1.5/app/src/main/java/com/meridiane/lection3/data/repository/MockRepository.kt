package com.meridiane.lection3.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.meridiane.lection3.R
import com.meridiane.lection3.domain.models.Order
import com.meridiane.lection3.domain.models.Product
import com.meridiane.lection3.domain.models.Profile
import com.meridiane.lection3.domain.repository.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MockRepository :
    InterfaceGetLoginRepository,
    InterfaceGetProductsRepository,
    InterfaceGetProfileRepository,
    InterfaceGetProductDetailsRepository,
    InterfaceGetActiveOrdersRepository{

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

    override suspend fun getDetailsProduct(id: String): Flow<Product> = flow {
        getProducts2().forEach { if (it.id == id) emit(it) }
    }

    override suspend fun getProducts2(): List<Product> {
        randomDelay()
        return listOf(
            Product(
                "1",
                "Футболка серая",
                "Футболка ",
                R.drawable.reposit_id1
            ),
            Product(
                "2",
                "Футболка черная",
                "Футболка ",
                R.drawable.reposit_id2
            ),
            Product(
                "3",
                "Футболка зелёная",
                "Футболка ",
                R.drawable.reposit_id3
            ),
            Product(
                "4",
                "Футболка желтая",
                "Футболка ",
                R.drawable.reposit_id4
            ),
            Product(
                "5",
                "Футболка красная",
                "Футболка ",
                R.drawable.reposit_id5
            ),
            Product(
                "6",
                "Бейсболка черная",
                "Бейсболка ",
                R.drawable.reposit_id6
            ),
            Product(
                "7",
                "Бейсболка серая",
                "Бейсболка ",
                R.drawable.reposit_id7
            ),
            Product(
                "8",
                "Бейсболка красная",
                "Бейсболка ",
                R.drawable.reposit_id8
            ),
            Product(
                "9",
                "Бейсболка желтая",
                "Бейсболка ",
                R.drawable.reposit_id9
            ),
            Product(
                "10",
                "Бейсболка зеленая",
                "Бейсболка ",
                R.drawable.reposit_id10
            ),
            Product(
                "11",
                "Шапка черная",
                "Шапка ",
                R.drawable.reposit_id11
            ),
            Product(
                "12",
                "Шапка серая",
                "Шапка ",
                R.drawable.reposit_id12
            ),
            Product(
                "13",
                "Шапка красная",
                "Шапка ",
                R.drawable.reposit_id13
            ),
            Product(
                "14",
                "Шапка желтая",
                "Шапка ",
                R.drawable.reposit_id14
            ),
            Product(
                "15",
                "Шапка синяя",
                "Шапка ",
                R.drawable.reposit_id15
            )
        )
    }

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
            ),
            Order(
                "5",
                "Заказ №123 от 19.09.21 18:03",
                "В работе",
                "4 × M • Nike Tampa Bay Buccaneers Super Bowl LV",
                "Дата доставки: 24.09.21 в 16:00",
                "Адрес доставки: г. Саранск, ул. Демократическая, 14",
                R.drawable.reposit_id5
            )
        )
    }

   override suspend fun getSizeAllOrder() : Int =
        getAllOrder().size



   override suspend fun getSizeActiveOrder() : Int =
        getActiveOrder().size


    override fun getList(): Flow<PagingData<Product>> {
        return Pager(
            config = PagingConfig(
                pageSize = 2,
                enablePlaceholders = false,
                initialLoadSize = 2,
                prefetchDistance = 2 / 2
            ),
            pagingSourceFactory = {
                PageSource(this)
            }
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

//    private fun randomResult(data: List<Product>): Result<List<Product>> =
//        when ((0..100).random()) {
//            in 0..33 -> Result.failure(RuntimeException())
//            in 34..67 -> Result.success(data)
//            else -> Result.success(emptyList())
//        }
}

