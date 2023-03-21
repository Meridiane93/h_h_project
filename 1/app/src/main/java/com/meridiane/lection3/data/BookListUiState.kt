package com.meridiane.lection4.data.getBookData

import com.meridiane.lection4.data.getBookData.BookAndAvailability

data class BookListUiState (
    val name: String? = null,
    val books: List<BookAndAvailability>? = null,
    val error: String? = null
)