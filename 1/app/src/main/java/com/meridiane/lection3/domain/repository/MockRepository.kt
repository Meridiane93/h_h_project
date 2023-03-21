package com.meridiane.lection3.domain.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.meridiane.lection3.R
import com.meridiane.lection3.data.Product
import com.meridiane.lection3.domain.PageSource
import com.meridiane.lection3.domain.ProductLoader
import com.meridiane.lection4.data.getBookData.Author
import com.meridiane.lection4.data.getBookData.Book
import com.meridiane.lection4.data.getBookData.BookAvailability
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.UUID
import kotlin.random.Random

class MockRepository {
    companion object {
        private val bookIds = (0..4).map {
            UUID.randomUUID().toString()
        }

        private val authorIds = (0..1).map {
            UUID.randomUUID().toString()
        }
    }

    suspend fun getBooks(): Result<List<Book>> {
        randomDelay()
        return randomResult(
            listOf(
                Book(
                    bookIds[0],
                    "The Hobbit",
                    authorIds[0]
                ),
                Book(
                    bookIds[1],
                    "The Winds of Winter",
                    authorIds[1]
                ),
                Book(
                    bookIds[2],
                    "The Silmarillion",
                    authorIds[0]
                ),
                Book(
                    bookIds[3],
                    "The Return of the King",
                    authorIds[0]
                ),
                Book(
                    bookIds[4],
                    "A Dream of Spring",
                    authorIds[1]
                ),
            )
        )
    }

    suspend fun getAuthors(): Result<List<Author>> {
        randomDelay()
        return randomResult(
            listOf(
                Author(
                    authorIds[0],
                    "J. R. R. Tolkien"
                ),
                Author(
                    authorIds[1],
                    "George R. R. Martin"
                ),
            )
        )
    }

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

    suspend fun getAvailability(): Result<List<BookAvailability>> {
        randomDelay()
        return randomResult(
            bookIds.map { bookId ->
                BookAvailability(
                    bookId = bookId,
                    inStock = Random.nextBoolean()
                )
            }
        )
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

