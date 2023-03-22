package com.meridiane.lection3.domain.repository

import com.meridiane.lection3.R
import com.meridiane.lection3.data.Product
import kotlinx.coroutines.delay

class MockRepository {

    suspend fun getProducts(): Result<List<Product>> {
        randomDelay()
        return randomResult(
            listOf(
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
        )

    }

    suspend fun getLogin(): Result<String> {
        delay(3000L)
        return randomResult("is success")
    }

    private suspend fun randomDelay() {
        delay((100L..1000L).random())
    }

    private fun <T> randomResult(data: T): Result<T> =
        if ((0..100).random() < 5) {
            Result.failure(RuntimeException())
        } else {
            Result.success(data)
        }
}

